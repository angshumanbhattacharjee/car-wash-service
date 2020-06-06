package com.car.wash.emailService;

import java.util.List;

import com.car.wash.model.CarWashModel;

public interface SendEmailService {

	public void sendEmailToWashers(List<String> getWashers, CarWashModel model, Object object, Object object2) throws Exception;

	public void sendMailToAdmin(Object customerName, Object washerName, List<String> adminEmailId, Object carModelName, CarWashModel model) throws Exception;

	public void sendWasherResponseToCustomer(Object customerEmailId, Object customerName, Object washerName, Object carModelName, CarWashModel carWashModel) throws Exception;

	public void sendWashDetailsToWasher(Object customerName, Object washerName, Object washerEmailId, Object carModelName, CarWashModel carWashModel) throws Exception;
	
	

}
