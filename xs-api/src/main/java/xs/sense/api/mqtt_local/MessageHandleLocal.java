package xs.sense.api.mqtt_local;

import org.springframework.messaging.MessagingException;

public interface MessageHandleLocal {

	void handleMessageLocal(String message) throws MessagingException;
	
}
