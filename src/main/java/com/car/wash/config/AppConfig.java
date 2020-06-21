package com.car.wash.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
	
	@Value("${data.transform.queue}")
	private String transformQueue;
	
	@Value("${user.profile.update.queue}")
	private String userWashCountUpdateQueue;
	
	@Value("${spring.rabbitmq.host}")
	private String rabbitHostName;
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@Bean
	public RabbitTemplate getRabbitTemplate(ConnectionFactory connectionFactory) {
		return new RabbitTemplate(connectionFactory);
	}
	
	@Bean
    public Queue queue() {
        return new Queue(this.transformQueue, true);
    }
	
	@Bean
    public Queue washCountqueue() {
        return new Queue(this.userWashCountUpdateQueue, true);
    }
	
	@Bean(name = "rabbit-mq")
	public CachingConnectionFactory getConnectionFactory() {
		CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
		cachingConnectionFactory.setHost(rabbitHostName);
		return cachingConnectionFactory;
	}

}
