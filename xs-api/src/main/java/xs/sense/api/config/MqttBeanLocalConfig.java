package xs.sense.api.config;


import com.google.common.util.concurrent.ThreadFactoryBuilder;
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
import xs.sense.api.mqtt_local.DataServiceLocal;
import xs.sense.api.mqtt_local.MessageHandleLocal;
import xs.sense.api.mqtt_local.XsCollectionHandlerLocal;

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
public class MqttBeanLocalConfig {
	@Value("${server.port}")
	private int port;

	@Value("${mqtt-local.host}")
	private String host;
	@Value("${mqtt-local.username}")
	private String username;
	@Value("${mqtt-local.password}")
	private String password;

	@Value("${mqtt-local.group}")
	private String group;

	@Value("${mqtt-local.SendRdInfo}")
	private String SendRdInfo;

	//TRXX装卸载数据
	@Value("${mqtt-local.RFIDLoadReport}")
	private String RFIDLoadReport;

	//TRXX状态信息数据
	@Value("${mqtt-local.DeviceStatus}")
	private String DeviceStatus;

//	TR90按键事件
	@Value("${mqtt-local.RFIDKeyEvent}")
	private String RFIDKeyEvent;

//	灯控制事件返回
	@Value("${mqtt-local.deviceControlLightResp}")
	private String deviceControlLightResp;


	/**
	 * mqtt工厂
	 * @return
	 */
	@Bean
	public MqttPahoClientFactory mqttClientFactoryPubLocal() {
		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
		factory.setServerURIs(host);
		factory.setUserName(username);
		factory.setPassword(password);
		return factory;
	}
	@Bean
	public ThreadFactory createThreadFactoryPubLocal(){
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
	public IntegrationFlow mqttOutFlowLocal() {

		return IntegrationFlows.from(outChannelLocal())
				.channel(MessageChannels.executor(new ThreadPoolExecutor(5, 200,
						60000L, TimeUnit.MILLISECONDS,
						new LinkedBlockingQueue<Runnable>(), createThreadFactoryPubLocal(), new ThreadPoolExecutor.AbortPolicy())))
				.handle(mqttOutboundLocal())
				.get();
	}

	@Bean
	public MessageChannel outChannelLocal() {
		return new DirectChannel();
	}

	@Bean
	public MessageHandler mqttOutboundLocal() {
		MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler("localSensePub_"+port+System.currentTimeMillis(), mqttClientFactoryPubLocal());
		messageHandler.setAsync(true);
		messageHandler.setConverter(new DefaultPahoMessageConverter());
//		messageHandler.setDefaultTopic("/sense/serail/rs01/1");
		messageHandler.setDefaultTopic(SendRdInfo);
		messageHandler.setDefaultQos(2);
		return messageHandler;
	}
	@Bean
	public IntegrationFlow mqttOutFlowRainLocal() {
//		console input
		return IntegrationFlows.from(CharacterStreamReadingMessageSource.stdin(),
				e -> e.poller(Pollers.fixedDelay(1000)))
				.transform(p -> p + " --------sent to MQTT----------")
				.handle(mqttOutboundLocal())
				.get();

//		return IntegrationFlows.from(outChannelRain())
//				.channel(MessageChannels.executor(new ThreadPoolExecutor(5, 200,
//						0L, TimeUnit.MILLISECONDS,
//						new LinkedBlockingQueue<Runnable>(), createThreadFactoryPub(), new ThreadPoolExecutor.AbortPolicy())))
//				.handle(mqttOutboundRain())
//				.get();
	}

	@Bean
	public MessageChannel outChannelRainLocal() {
		return new DirectChannel();
	}

	@Bean
	public MessageHandler mqttOutboundRainLocal() {
		MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler("localSensePubRain_"+port+System.currentTimeMillis(), mqttClientFactoryPubLocal());
		messageHandler.setAsync(false);
		DefaultPahoMessageConverter defaultPahoMessageConverter = new DefaultPahoMessageConverter();
//		defaultPahoMessageConverter.setPayloadAsBytes(true);
		messageHandler.setConverter(defaultPahoMessageConverter);
//		messageHandler.setConverter(new DefaultPahoMessageConverter(0,true));
		messageHandler.setDefaultTopic(SendRdInfo);
		return messageHandler;
	}


	@Bean
	public MqttPahoClientFactory mqttClientFactorySubLocal() {
		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
		factory.setServerURIs(host);
		factory.setUserName(username);
		factory.setPassword(password);
		return factory;
	}
	@Bean
	public ThreadFactory createThreadFactorySubLocal(){
		return new ThreadFactoryBuilder()
				.setNameFormat("sub-pool-%d").build();
	}

	/**
	 * 订阅工厂，订阅不同主题  TRXX 系列  上报RFID信息主题
	 * @return
	 */
	@Bean
	public MessageProducerSupport mqttInboundSendRdInfoLocal() {
		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter("localSenseSub_"+port+System.currentTimeMillis(),
//				mqttClientFactorySubLocal(), "/sense/serail/rs01/#");
				mqttClientFactorySubLocal(), SendRdInfo);
		adapter.setCompletionTimeout(5000);
		DefaultPahoMessageConverter defaultPahoMessageConverter = new DefaultPahoMessageConverter();
//		defaultPahoMessageConverter.setPayloadAsBytes(true);
		adapter.setConverter(defaultPahoMessageConverter);
		adapter.setQos(2);
		return adapter;
	}

	/**
	 * 消息订阅（通过路由订阅的主题，分发不同通道进行处理不同主题不同数据）
	 * @return
	 */
	@Bean
	public IntegrationFlow mqttInFlowSendRdInfoLocal() {

		return IntegrationFlows.from(mqttInboundSendRdInfoLocal())
				.channel(MessageChannels.executor(new ThreadPoolExecutor(5, 200,
						0L, TimeUnit.MILLISECONDS,
						new LinkedBlockingQueue<Runnable>(), createThreadFactorySubLocal(), new ThreadPoolExecutor.AbortPolicy())))
				.channel("SendRdInfo")
				.get();
	}

	@Bean
	@ServiceActivator(inputChannel = "SendRdInfo")
	public MessageHandleLocal xsDataSubProcessLocal() {
		return new MessageHandleLocal() {
			@Autowired
			private DataServiceLocal dataServiceLocal;
			private final Logger logger = LoggerFactory.getLogger(this.getClass());
			@Override
			public void handleMessageLocal(String message) throws MessagingException {
//					logger.info(message);
				XsCollectionHandlerLocal.swithHandlerLocal(message, dataServiceLocal,"SendRdInfo");
			}

		};
	}

	/**
	 * 订阅工厂，订阅不同主题  TRXX 系列  上报装卸载信息主题
	 * @return
	 */
	@Bean
	public MessageProducerSupport mqttInboundRFIDLoadReportLocal() {
		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter("localRFIDLoadSub_"+port+System.currentTimeMillis(),
//				mqttClientFactorySubLocal(), "/sense/serail/rs01/#");
				mqttClientFactorySubLocal(), RFIDLoadReport);
		adapter.setCompletionTimeout(5000);
		DefaultPahoMessageConverter defaultPahoMessageConverter = new DefaultPahoMessageConverter();
//		defaultPahoMessageConverter.setPayloadAsBytes(true);
		adapter.setConverter(defaultPahoMessageConverter);
		adapter.setQos(2);
		return adapter;
	}

	/**
	 * 消息订阅（通过路由订阅的主题，分发不同通道进行处理不同主题不同数据）
	 * @return
	 */
	@Bean
	public IntegrationFlow mqttInFlowRFIDLoadReportLocal() {

		return IntegrationFlows.from(mqttInboundRFIDLoadReportLocal())
				.channel(MessageChannels.executor(new ThreadPoolExecutor(5, 200,
						0L, TimeUnit.MILLISECONDS,
						new LinkedBlockingQueue<Runnable>(), createThreadFactorySubLocal(), new ThreadPoolExecutor.AbortPolicy())))
				.channel("RFIDLoadReportLocal")
				.get();
	}

	@Bean
	@ServiceActivator(inputChannel = "RFIDLoadReportLocal")
	public MessageHandleLocal xsDataSubRFIDLoadReportLocal() {
		return new MessageHandleLocal() {
			@Autowired
			private DataServiceLocal dataServiceLocal;
			private final Logger logger = LoggerFactory.getLogger(this.getClass());
			@Override
			public void handleMessageLocal(String message) throws MessagingException {
					logger.info(message);
				XsCollectionHandlerLocal.swithHandlerLocal(message, dataServiceLocal,"RFIDLoadReportLocal");
			}

		};
	}


	/**
	 * 订阅工厂，订阅不同主题  TRXX 系列  上报设备状态信息主题
	 * @return
	 */
	@Bean
	public MessageProducerSupport mqttInboundDeviceStatusReportLocal() {
		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter("localDeviceStatusSub_"+port+System.currentTimeMillis(),
//				mqttClientFactorySubLocal(), "/sense/serail/rs01/#");
				mqttClientFactorySubLocal(), DeviceStatus);
		adapter.setCompletionTimeout(5000);
		DefaultPahoMessageConverter defaultPahoMessageConverter = new DefaultPahoMessageConverter();
//		defaultPahoMessageConverter.setPayloadAsBytes(true);
		adapter.setConverter(defaultPahoMessageConverter);
		adapter.setQos(2);
		return adapter;
	}

	/**
	 * 消息订阅（通过路由订阅的主题，分发不同通道进行处理不同主题不同数据）
	 * @return
	 */
	@Bean
	public IntegrationFlow mqttInFlowDeviceStatusReportLocal() {

		return IntegrationFlows.from(mqttInboundDeviceStatusReportLocal())
				.channel(MessageChannels.executor(new ThreadPoolExecutor(5, 200,
						0L, TimeUnit.MILLISECONDS,
						new LinkedBlockingQueue<Runnable>(), createThreadFactorySubLocal(), new ThreadPoolExecutor.AbortPolicy())))
				.channel("DeviceStatusReportLocal")
				.get();
	}

	@Bean
	@ServiceActivator(inputChannel = "DeviceStatusReportLocal")
	public MessageHandleLocal xsDataSubDeviceStatusReportLocal() {
		return new MessageHandleLocal() {
			@Autowired
			private DataServiceLocal dataServiceLocal;
			private final Logger logger = LoggerFactory.getLogger(this.getClass());
			@Override
			public void handleMessageLocal(String message) throws MessagingException {
//					logger.info(message);
				XsCollectionHandlerLocal.swithHandlerLocal(message, dataServiceLocal,"DeviceStatusReportLocal");
			}

		};
	}
	/**
	 * 订阅工厂，订阅不同主题  TRXX 系列  上报设备状态信息主题
	 * @return
	 */
	@Bean
	public MessageProducerSupport mqttInboundRFIDKeyEventLocal() {
		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter("localRFIDKeyEventSub_"+port+System.currentTimeMillis(),
//				mqttClientFactorySubLocal(), "/sense/serail/rs01/#");
				mqttClientFactorySubLocal(), RFIDKeyEvent);
		adapter.setCompletionTimeout(5000);
		DefaultPahoMessageConverter defaultPahoMessageConverter = new DefaultPahoMessageConverter();
//		defaultPahoMessageConverter.setPayloadAsBytes(true);
		adapter.setConverter(defaultPahoMessageConverter);
		adapter.setQos(1);
		return adapter;
	}

	/**
	 * 消息订阅（通过路由订阅的主题，分发不同通道进行处理不同主题不同数据）
	 * @return
	 */
	@Bean
	public IntegrationFlow mqttInFlowRFIDKeyEventLocal() {

		return IntegrationFlows.from(mqttInboundRFIDKeyEventLocal())
				.channel(MessageChannels.executor(new ThreadPoolExecutor(5, 200,
						0L, TimeUnit.MILLISECONDS,
						new LinkedBlockingQueue<Runnable>(), createThreadFactorySubLocal(), new ThreadPoolExecutor.AbortPolicy())))
				.channel("RFIDKeyEventLocal")
				.get();
	}

	@Bean
	@ServiceActivator(inputChannel = "RFIDKeyEventLocal")
	public MessageHandleLocal xsDataSubRFIDKeyEventLocal() {
		return new MessageHandleLocal() {
			@Autowired
			private DataServiceLocal dataServiceLocal;
			private final Logger logger = LoggerFactory.getLogger(this.getClass());
			@Override
			public void handleMessageLocal(String message) throws MessagingException {
//					logger.info(message);
				XsCollectionHandlerLocal.swithHandlerLocal(message, dataServiceLocal,"RFIDKeyEventLocal");
			}

		};
	}
	/**
	 * 订阅工厂，订阅不同主题  TRXX 系列  上报设备状态信息主题
	 * @return
	 */
	@Bean
	public MessageProducerSupport mqttInbounddeviceControlLightRespLocal() {
		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter("localLightControlRespSub_"+port+System.currentTimeMillis(),
//				mqttClientFactorySubLocal(), "/sense/serail/rs01/#");
				mqttClientFactorySubLocal(), deviceControlLightResp);
		adapter.setCompletionTimeout(5000);
		DefaultPahoMessageConverter defaultPahoMessageConverter = new DefaultPahoMessageConverter();
//		defaultPahoMessageConverter.setPayloadAsBytes(true);
		adapter.setConverter(defaultPahoMessageConverter);
		adapter.setQos(1);
		return adapter;
	}

	/**
	 * 消息订阅（通过路由订阅的主题，分发不同通道进行处理不同主题不同数据）
	 * @return
	 */
	@Bean
	public IntegrationFlow mqttInFlowdeviceControlLightRespLocal() {

		return IntegrationFlows.from(mqttInbounddeviceControlLightRespLocal())
				.channel(MessageChannels.executor(new ThreadPoolExecutor(5, 200,
						0L, TimeUnit.MILLISECONDS,
						new LinkedBlockingQueue<Runnable>(), createThreadFactorySubLocal(), new ThreadPoolExecutor.AbortPolicy())))
				.channel("deviceControlLightResp")
				.get();
	}

	@Bean
	@ServiceActivator(inputChannel = "deviceControlLightResp")
	public MessageHandleLocal xsDataSubdeviceControlLightRespLocal() {
		return new MessageHandleLocal() {
			@Autowired
			private DataServiceLocal dataServiceLocal;
			private final Logger logger = LoggerFactory.getLogger(this.getClass());
			@Override
			public void handleMessageLocal(String message) throws MessagingException {
//					logger.info(message);
				XsCollectionHandlerLocal.swithHandlerLocal(message, dataServiceLocal,"deviceControlLightResp");
			}

		};
	}
}
