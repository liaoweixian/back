package xs.sense.api.mqtt;

import org.springframework.messaging.MessagingException;

public interface MessageHandle {

	void handleMessage(String message) throws MessagingException;
	
}
