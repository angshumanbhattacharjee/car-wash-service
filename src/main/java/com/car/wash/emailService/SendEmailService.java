package com.car.wash.emailService;

import java.util.List;

import com.car.wash.model.CarWashModel;

public interface SendEmailService {

	public void sendEmailToWashers(List<String> getWashers, CarWashModel model, Object object, Object object2) throws Exception;

	public void sendMailToAdmin(CarWashModel model);
	
	

}
