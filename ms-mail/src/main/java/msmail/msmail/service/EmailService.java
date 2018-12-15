package msmail.msmail.service;

import msmail.msmail.entity.dto.User;

public interface EmailService {
	
	void sendSimpleMessage(User input);

}
