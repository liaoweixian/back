package xs.rfid.config;


import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.stream.CharacterStreamReadingMessageSource;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import xs.rfid.modules.mqtt.MessageHandle;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * mqtt消息服务配置工厂，包括订阅、处理、推送服务
 * @author mikehhuang
 *
 */
@Configuration
@EnableIntegration
public class MqttBeanConfig {
	@Value("${server.port}")
	private int port;

	@Value("${mqtt.host}")
	private String host;
	@Value("${mqtt.username}")
	private String username;
	@Value("${mqtt.password}")
	private String password;
	@Value("${mqtt.xsDataTopc}")
	private String xsDataTopc;
	@Value("${mqtt.group}")
	private String group;


	@Bean
	public MqttConnectOptions getMqttConnectOptions() {
		MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
		mqttConnectOptions.setUserName(username);
		mqttConnectOptions.setKeepAliveInterval(60);
		mqttConnectOptions.setMaxInflight(100000);
		mqttConnectOptions.setPassword(password.toCharArray());
		mqttConnectOptions.setServerURIs(new String[]{host});
//		mqttConnectOptions.setConnectionTimeout(completionTimeout);
		return mqttConnectOptions;
	}

	/**
	 * mqtt工厂
	 * @return
	 */

	@Bean
	public MqttPahoClientFactory mqttClientFactoryPub() {
		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
//		factory.setServerURIs(host);
//		factory.setUserName(username);
//		factory.setPassword(password);
		factory.setConnectionOptions(getMqttConnectOptions());
		return factory;
	}
	@Bean
	public ThreadFactory createThreadFactoryPub(){
		return new ThreadFactoryBuilder()
				.setNameFormat("pub-pool-%d").build();
	}

	/**
	 * 消息订阅（通过路由订阅的主题，分发不同通道进行处理不同主题不同数据）
	 * @return
	 */


	private LoggingHandler logger() {
		LoggingHandler loggingHandler = new LoggingHandler("INFO");
		loggingHandler.setLoggerName("rfid");
		return loggingHandler;
	}



	/**
	 * 消息发送
	 * @return
	 */
	@Bean
	public IntegrationFlow mqttOutFlow() {

		return IntegrationFlows.from(outChannel())
				.channel(MessageChannels.executor(new ThreadPoolExecutor(5, 200,
						60000L, TimeUnit.MILLISECONDS,
						new LinkedBlockingQueue<Runnable>(), createThreadFactoryPub(), new ThreadPoolExecutor.AbortPolicy())))
				.handle(mqttOutbound())
				.get();
	}

	@Bean
	public MessageChannel outChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageHandler mqttOutbound() {
		MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler("noticePub_"+port+System.currentTimeMillis(), mqttClientFactoryPub());
		messageHandler.setAsync(true);
		messageHandler.setConverter(new DefaultPahoMessageConverter());
		messageHandler.setDefaultTopic("/sense/serail/rs01/1");
//		messageHandler.setDefaultTopic(xsDataTopc);
		messageHandler.setDefaultQos(0);
		return messageHandler;
	}
	@Bean
	public IntegrationFlow mqttOutFlowRain() {
//		console input
		return IntegrationFlows.from(CharacterStreamReadingMessageSource.stdin(),
				e -> e.poller(Pollers.fixedDelay(1000)))
				.transform(p -> p + " --------sent to MQTT----------")
				.handle(mqttOutbound())
				.get();

//		return IntegrationFlows.from(outChannelRain())
//				.channel(MessageChannels.executor(new ThreadPoolExecutor(5, 200,
//						0L, TimeUnit.MILLISECONDS,
//						new LinkedBlockingQueue<Runnable>(), createThreadFactoryPub(), new ThreadPoolExecutor.AbortPolicy())))
//				.handle(mqttOutboundRain())
//				.get();
	}

	@Bean
	public MessageChannel outChannelRain() {
		return new DirectChannel();
	}

	@Bean
	public MessageHandler mqttOutboundRain() {
		MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler("noticePubRain_"+port+System.currentTimeMillis(), mqttClientFactoryPub());
		messageHandler.setAsync(false);
		DefaultPahoMessageConverter defaultPahoMessageConverter = new DefaultPahoMessageConverter();
//		defaultPahoMessageConverter.setPayloadAsBytes(true);
		messageHandler.setConverter(defaultPahoMessageConverter);
//		messageHandler.setConverter(new DefaultPahoMessageConverter(0,true));
		messageHandler.setDefaultTopic(xsDataTopc);
		return messageHandler;
	}


	@Bean
	public MqttPahoClientFactory mqttClientFactorySub() {
		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
		factory.setServerURIs(host);
		factory.setUserName(username);
		factory.setPassword(password);
		return factory;
	}
	@Bean
	public ThreadFactory createThreadFactorySub(){
		return new ThreadFactoryBuilder()
				.setNameFormat("sub-pool-%d").build();
	}

	/**
	 * 订阅工厂，订阅不同主题
	 * @return
	 */
	@Bean
	public MessageProducerSupport mqttInboundModbus() {
		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter("noticeSub_"+port+System.currentTimeMillis(),
				mqttClientFactorySub(), "/sense/serail/rs01/#");
//				mqttClientFactorySub(), xsDataTopc);
		adapter.setCompletionTimeout(5000);
		DefaultPahoMessageConverter defaultPahoMessageConverter = new DefaultPahoMessageConverter();
//		defaultPahoMessageConverter.setPayloadAsBytes(true);
		adapter.setConverter(defaultPahoMessageConverter);
		adapter.setQos(0);
		return adapter;
	}

	/**
	 * 消息订阅（通过路由订阅的主题，分发不同通道进行处理不同主题不同数据）
	 * @return
	 */
	@Bean
	public IntegrationFlow mqttInFlowModbus() {
//		return IntegrationFlows.from(mqttInboundUps())
//				.transform(p -> p + ", ----------------------received from MQTT")
//				.handle(logger())
//				.get();

		return IntegrationFlows.from(mqttInboundModbus())
				.channel(MessageChannels.executor(new ThreadPoolExecutor(5, 200,
						0L, TimeUnit.MILLISECONDS,
						new LinkedBlockingQueue<Runnable>(), createThreadFactorySub(), new ThreadPoolExecutor.AbortPolicy())))
				.channel("xsdatasub")
				.get();
	}

	/*@Bean
	@ServiceActivator(inputChannel = "xsdatasub")
	public MessageHandle xsDataSubProcess() {
		return new MessageHandle() {
			@Autowired
			private DataService dataService;
			private final Logger logger = LoggerFactory.getLogger(this.getClass());
			@Override
			public void handleMessage(String message) throws MessagingException {
//					logger.info(message);
				XsCollectionHandler.swithHandler(message, dataService);
			}

		};
	}*/
}