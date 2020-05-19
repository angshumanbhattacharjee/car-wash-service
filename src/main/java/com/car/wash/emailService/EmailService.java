package com.car.wash.emailService;

import java.util.List;

import com.car.wash.model.CarWashModel;

public interface EmailService {

	void sendEmailToWashers(List<String> getWashers);

	void sendMailToAdmin(CarWashModel model);
	
	

}
