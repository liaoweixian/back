package xs.rfid.modules.mqtt;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Header;

@MessagingGateway(defaultRequestChannel = "outChannel")
public interface SendMessage {

	public void send(String message);
	
	public void send(String message, @Header("mqtt_topic") String topic);

	public void send(byte[] message, @Header("mqtt_topic") String topic);
}
