package msmail.msmail.kafka.consumer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import msmail.msmail.entity.dto.User;


@EnableKafka
@Configuration
public class ReceiverConfig {
	@Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String consumerGroupId;


    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        return props;
    }
    
    @Bean
    public ConsumerFactory<String, User> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs(), new StringDeserializer(),
                new JsonDeserializer<>(User.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, User> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, User> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        
        factory.setErrorHandler(new ErrorHandler() {
            @Override
            public void handle(Exception thrownException, List<ConsumerRecord<?, ?>> records, Consumer<?, ?> consumer, MessageListenerContainer container) {
                String s = thrownException.getMessage().split("Error deserializing key/value for partition ")[1].split(". If needed, please seek past the record to continue consumption.")[0];
                System.out.println("sssssss111111::::"+s);
                String topics = s.split("-")[0];
                System.out.println("topics11111111:::::::"+topics);
                int offset = Integer.valueOf(s.split("offset ")[1]);
                System.out.println("offset::::::"+offset);
                int partition = Integer.valueOf(s.split("-")[1].split(" at")[0]);
                System.out.println("partition::::::"+partition);
                TopicPartition topicPartition = new TopicPartition(topics, partition);
                //log.info("Skipping " + topic + "-" + partition + " offset " + offset);
                consumer.seek(topicPartition, offset + 1);
                System.out.println("OKKKKK111111");
            }

        

            @Override
            public void handle(Exception e, ConsumerRecord<?, ?> consumerRecord, Consumer<?,?> consumer) {
                String s = e.getMessage().split("Error deserializing key/value for partition ")[1].split(". If needed, please seek past the record to continue consumption.")[0];
                String topics = s.split("-")[0];
                int offset = Integer.valueOf(s.split("offset ")[1]);
                int partition = Integer.valueOf(s.split("-")[1].split(" at")[0]);

                TopicPartition topicPartition = new TopicPartition(topics, partition);
                //log.info("Skipping " + topic + "-" + partition + " offset " + offset);
                consumer.seek(topicPartition, offset + 1);
                System.out.println("OKKKKK222222222");
                


            }

			@Override
			public void handle(Exception thrownException, ConsumerRecord<?, ?> data) {
				// TODO Auto-generated method stub
				
			}
        });
        

        return factory;
    }

   
    @Bean
    public Receiver receiver() {
    	System.out.println("receiver::::::::::::::::");
        return new Receiver();
    }

}
