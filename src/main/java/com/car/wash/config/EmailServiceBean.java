package com.car.wash.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.car.wash.emailService.EmailService;
import com.car.wash.model.CarWashModel;

@Configuration
public class EmailServiceBean {
		
		@Bean
		public EmailService emailService() {
			return new EmailService() {

				@Override
				public void sendEmailToWashers(List<String> getWashers) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void sendMailToAdmin(CarWashModel model) {
					// TODO Auto-generated method stub
					
				}};
		}
}
