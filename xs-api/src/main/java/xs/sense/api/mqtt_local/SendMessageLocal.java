package xs.sense.api.mqtt_local;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Header;

@MessagingGateway(defaultRequestChannel = "outChannelLocal")
public interface SendMessageLocal {

	public void send(String message);
	
	public void send(String message, @Header("mqtt_topic") String topic);

	public void send(byte[] message, @Header("mqtt_topic") String topic);
}
