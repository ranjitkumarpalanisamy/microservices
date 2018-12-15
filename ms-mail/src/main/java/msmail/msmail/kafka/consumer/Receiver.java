package msmail.msmail.kafka.consumer;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

import msmail.msmail.entity.dto.User;
import msmail.msmail.service.EmailService;

public class Receiver {
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);

    private CountDownLatch latch = new CountDownLatch(1);

    @Autowired
    private EmailService emailService;
    
    

    @KafkaListener(id="${spring.kafka.consumer.group-id}",topics = "${spring.kafka.topic.userCreated}")
    public void receive(User payload) {
        LOGGER.info("received payload='{}'", payload);
        System.out.println("payload:::::::::::"+payload);
        emailService.sendSimpleMessage(payload);
        latch.countDown();
    }
    
    @Bean
    public StringJsonMessageConverter jsonConverter() {
        return new StringJsonMessageConverter();
    }

}
