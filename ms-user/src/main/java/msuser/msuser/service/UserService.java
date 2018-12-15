package msuser.msuser.service;

import msuser.msuser.entity.User;

public interface UserService {
	
	 	User registerUser(User input);

	    Iterable<User> findAll();

}

