package msuser.msuser.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import msuser.msuser.entity.User;
import msuser.msuser.kafka.producer.Sender;
import msuser.msuser.repository.UserRepository;


@Service("userService")
public class UserServiceImpl implements UserService {
	
	    @Value("${spring.kafka.topic.userCreated}")
	    private String USER_CREATED_TOPIC;

	    @Autowired
	    private UserRepository userRepository;
	    
	    @Autowired
	    private Sender sender;

	    /*@Autowired
	    UserServiceImpl(UserRepository userRepository, Sender sender) {
	        this.userRepository = userRepository;
	        this.sender = sender;
	    }*/

	    @Override
	    public User registerUser(User input) {
	        User createdUser = userRepository.save(input);
	        sender.send(USER_CREATED_TOPIC, createdUser);
	        return createdUser;
	    }

	    @Override
	    public Iterable<User> findAll() {
	        return userRepository.findAll();
	    }

}

