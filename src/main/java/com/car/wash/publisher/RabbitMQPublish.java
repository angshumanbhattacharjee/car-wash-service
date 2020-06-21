package com.car.wash.publisher;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.car.wash.constants.IConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class RabbitMQPublish {
	
	@Value("${data.transform.queue}")
	private String transformQueue;
	
	@Value("${user.profile.update.queue}")
	private String userWashCountQueue;
	
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private AmqpTemplate template;
	
	public void publishUserReviewDTO(Object userReviewDTO) {
		try {
			if (userReviewDTO != null) {
				String json = null;
				if(!(userReviewDTO instanceof String))
					json = mapper.writeValueAsString(userReviewDTO);
				else 
					json = userReviewDTO.toString();
				
				template.convertAndSend(transformQueue, json);				
				log.info(String.format("Published Message to Queue:%s with %s", transformQueue, json));
			}
		} catch (Exception e) {
			log.info(IConstants.ERROR_IN_PROCESSING_REQUEST, e.getMessage());
		}
	}

	public void publishUserWashCountDTO(Object washCountDTO) {
		try {
			if (washCountDTO != null) {
				String json = null;
				if (!(washCountDTO instanceof String))
					json = mapper.writeValueAsString(washCountDTO);
				else
					json = washCountDTO.toString();
				
				template.convertAndSend(userWashCountQueue, json);
				log.info(String.format("Published Message to Queue:%s with %s", userWashCountQueue, json));
			}
		} catch (Exception e) {
			log.info(IConstants.ERROR_IN_PROCESSING_REQUEST, e.getMessage());
		}
	}	

}
