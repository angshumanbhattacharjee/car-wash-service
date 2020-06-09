/**
 * 
 */
package com.car.wash.emailServiceImpl;

import java.util.List;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.car.wash.constants.EmailServiceConstants;
import com.car.wash.constants.IConstants;
import com.car.wash.emailService.SendEmailService;
import com.car.wash.model.CarWashModel;
import com.car.wash.utility.CommonUtility;

/**
 * @author ANGSHUMAN
 *
 */
@Component
public class SendEmailServiceImpl implements SendEmailService {
	
	@Value("${mail.smtp.host}")
	private String mailSmtpHost;
	
	@Value("${mail.smtp.socketFactory.port}")
	private String socketFactoryPort;
	
	@Value("${mail.smtp.socketFactory.class}")
	private String socketFactoryClass;
	
	@Value("${mail.smtp.auth}")
	private String smtpAuth;
	
	@Value("${mail.smtp.port}")
	private String smtpPort;

	@Override
	public void sendEmailToWashers(List<String> getWashers, CarWashModel model, Object customerName, Object carManufacturerName) throws Exception {
		String mailSubject = null;
		String mailBody = null;
		try {
			mailSubject = prepareMailSubject(model);
			mailBody = prepareMailBody(model, customerName, carManufacturerName);
			for(int entry = 0; entry<getWashers.size(); entry++) {
				sendEmail(IConstants.FROM, IConstants.PASSWORD, getWashers.get(entry), mailSubject, mailBody);
			}
		} catch (Exception e) {
			throw e;
			// TODO: handle exception
		}
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void sendWasherResponseToCustomer(Object customerEmailId, Object customerName, Object washerName, Object carModelName,
			CarWashModel carWashModel) throws Exception {
		String mailSubjectForCustomer = null;
		String mailBodyForCustomer = null;
		try {
			mailSubjectForCustomer = EmailServiceConstants.MAIL_SUBJECT_FOR_CUSTOMER;
			mailBodyForCustomer = prepareMailBodyForCustomer(customerName, washerName, carModelName, carWashModel);
			sendEmail(IConstants.FROM, IConstants.PASSWORD, (String) customerEmailId, mailSubjectForCustomer, mailBodyForCustomer);
		} catch (Exception e) {
			throw e;
		}
		
	}
	
	@Override
	public void sendWashDetailsToWasher(Object customerName, Object washerName, Object washerEmailId,
			Object carModelName, CarWashModel carWashModel) throws Exception {
		String mailSubjectForWasher = null;
		String mailBodyForWasher = null;
		try {
			mailSubjectForWasher = prepareMailSubjectForWasher(carWashModel.getWashingId());
			mailBodyForWasher = prepareMailBodyForWasher(customerName, washerName, carModelName, carWashModel);
			sendEmail(IConstants.FROM, IConstants.PASSWORD, (String) washerEmailId, mailSubjectForWasher, mailBodyForWasher);
		} catch (Exception e) {
			throw e;
			// TODO: handle exception
		}
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void sendMailToAdmin(Object customerName, Object washerName, List<String> adminEmailId, Object carModelName, CarWashModel model)
			throws Exception {
		String mailSubjectForAdmin = null;
		String mailBodyForAdmin = null;
		try {
			mailSubjectForAdmin = prepareMailSubjectForAdmin(model.getWashingId());
			mailBodyForAdmin = prepareMailBodyForAdmin(customerName, washerName, carModelName, model);
			for(int entry = 0; entry<adminEmailId.size(); entry++) {
				sendEmail(IConstants.FROM, IConstants.PASSWORD, adminEmailId.get(entry), mailSubjectForAdmin, mailBodyForAdmin);
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	public void sendStartWashStatusToCustomer(Object washerName, Object customerName, Object customerEmailId, Object carModelName, CarWashModel carWashModel) throws Exception {
		String mailSubjectForCustomer = null;
		String mailBodyForCustomer = null;
		try {
			mailSubjectForCustomer = prepareStartWashSubject(carModelName);
			mailBodyForCustomer = prepareStartWashBody(washerName, customerName, carModelName);
			sendEmail(IConstants.FROM, IConstants.PASSWORD, (String) customerEmailId, mailSubjectForCustomer, mailBodyForCustomer);
		} catch (Exception e) {
			throw e;
			// TODO: handle exception
		}
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void sendEndWashStatusToCustomer(Object customerName, Object customerEmailId, Object washerName, Object carModelName, CarWashModel carWashModel) {
		String mailSubjectForCustomer = null;
		String mailBodyForCustomer = null;
		try {
			mailSubjectForCustomer = prepareEndWashSubjectForCustomer(carModelName);
			mailBodyForCustomer = prepareEndWashBodyForCustomer(customerName, washerName, carModelName);
			sendEmail(IConstants.FROM, IConstants.PASSWORD, (String) customerEmailId, mailSubjectForCustomer, mailBodyForCustomer);
		} catch (Exception e) {
			// TODO: handle exception
		}
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void sendEndWashStatusToWasher(Object washerName, Object washerEmailId, Object customerName, Object carName,
			CarWashModel carWashModel) {
		String mailSubjectForWasher = null;
		String mailBodyForWasher = null;
		try {
			mailSubjectForWasher = prepareEndWashSubjectForWasher(carWashModel.getWashingId());
			mailBodyForWasher = prepareEndWashBodyForWasher(washerName, customerName);
			sendEmail(IConstants.FROM, IConstants.PASSWORD, (String) washerEmailId, mailSubjectForWasher, mailBodyForWasher);
		} catch (Exception e) {
			// TODO: handle exception
		}
		// TODO Auto-generated method stub
		
	}

	private String prepareEndWashBodyForWasher(Object washerName, Object customerName) {
		String body = EmailServiceConstants.HI + (String) washerName + EmailServiceConstants.HTML_1 + EmailServiceConstants.COMPLETE_WASH_TEXT_FOR_WASHER + customerName + EmailServiceConstants.PROVIDE_REVIEW_WASHER;
		// TODO Auto-generated method stub
		return body;
	}

	private String prepareEndWashSubjectForWasher(String washingId) {
		String subject = EmailServiceConstants.WASH_COMPLETED_FOR_WASHER + washingId;
		// TODO Auto-generated method stub
		return subject;
	}

	private String prepareEndWashBodyForCustomer(Object customerName, Object washerName, Object carModelName) {
		String body = EmailServiceConstants.HI + (String) customerName + EmailServiceConstants.HTML_1 + (String) washerName + EmailServiceConstants.COMPLETE_WASH_TEXT + CommonUtility.getCurrentDateInString() + EmailServiceConstants.IST + EmailServiceConstants.PROVIDE_REVIEW + washerName;
		// TODO Auto-generated method stub
		return body;
	}

	private String prepareEndWashSubjectForCustomer(Object carModelName) {
		String subject = EmailServiceConstants.WASH_COMPLETED + (String) carModelName + EmailServiceConstants.CAR;
		// TODO Auto-generated method stub
		return subject;
	}

	private String prepareStartWashBody(Object washerName, Object customerName, Object carModelName) {
		String body = EmailServiceConstants.HI + (String) customerName + EmailServiceConstants.HTML_1 + (String) washerName + EmailServiceConstants.START_WASH_TEXT + CommonUtility.getCurrentDateInString() + EmailServiceConstants.IST;
		// TODO Auto-generated method stub
		return body;
	}

	private String prepareStartWashSubject(Object carModelName) {
		String subject = EmailServiceConstants.WASH_STARTED + (String) carModelName + EmailServiceConstants.CAR;
		// TODO Auto-generated method stub
		return subject;
	}

	private String prepareMailBodyForAdmin(Object customerName, Object washerName, Object carModelName, CarWashModel carWashModel) {
		String body = EmailServiceConstants.HI_ADMIN + (String) washerName + EmailServiceConstants.MAIL_BODY_1 + customerName + EmailServiceConstants.MAIL_BODY_2 + 
				EmailServiceConstants.WASH_DETAILS_1 + carWashModel.getWashPackage() + EmailServiceConstants.WASH_DETAILS_2 + 
				carWashModel.getExtraWashAddOns() + EmailServiceConstants.WASH_DETAILS_3 + (String) carModelName + 
				EmailServiceConstants.WASH_DETAILS_4 + carWashModel.getInfoNotes();
		// TODO Auto-generated method stub
		return body ;
	}

	private String prepareMailSubjectForAdmin(String washingId) {
		String subject = EmailServiceConstants.WASH_REQUEST_REJECTED_SUBJECT + washingId;
		// TODO Auto-generated method stub
		return subject;
	}

	private String prepareMailBodyForWasher(Object customerName, Object washerName, Object carModelName,
			CarWashModel carWashModel) {
		
		String body = EmailServiceConstants.HI + (String) washerName + EmailServiceConstants.HTML_1 + EmailServiceConstants.WASH_REQUEST_ACCEPTED_TEXT_FOR_WASHER + (String) customerName + 
				EmailServiceConstants.WASH_DETAILS_1 + carWashModel.getWashPackage() + EmailServiceConstants.WASH_DETAILS_2 + 
				carWashModel.getExtraWashAddOns() + EmailServiceConstants.WASH_DETAILS_3 + (String) carModelName + 
				EmailServiceConstants.WASH_DETAILS_4 + carWashModel.getInfoNotes();
		// TODO Auto-generated method stub
		return body ;
	}

	private String prepareMailSubjectForWasher(String washingId) {
		String subject = EmailServiceConstants.WASH_DETAILS_TEXT + washingId;
		// TODO Auto-generated method stub
		return subject ;
	}

	private String prepareMailBodyForCustomer(Object customerName, Object washerName, Object carModelName,
			CarWashModel carWashModel) {
		
		String body = EmailServiceConstants.HI + (String) customerName + EmailServiceConstants.HTML_1 + (String) washerName + EmailServiceConstants.WASH_REQUEST_ACCEPTED_TEXT + 
				EmailServiceConstants.WASH_DETAILS_1 + carWashModel.getWashPackage() + EmailServiceConstants.WASH_DETAILS_2 + 
				carWashModel.getExtraWashAddOns() + EmailServiceConstants.WASH_DETAILS_3 + (String) carModelName + 
				EmailServiceConstants.WASH_DETAILS_4 + carWashModel.getInfoNotes();
		
		return body;
	}

	private String prepareMailBody(CarWashModel model, Object customerName, Object carManufacturerName) {
		String body = IConstants.MAIL_BODY_1 + (String) customerName + IConstants.MAIL_BODY_2 + model.getWashPackage() + IConstants.MAIL_BODY_3 + (String) carManufacturerName + IConstants.MAIL_BODY_4;
		// TODO Auto-generated method stub
		return body;
	}

	private String prepareMailSubject(CarWashModel model) {
		String subject = IConstants.MAIL_SUBJECT + model.getWashingId();
		// TODO Auto-generated method stub
		return subject;
	}

	private void sendEmail(String fROM, String pASSWORD, String To, String subject, String body) throws Exception {
		Properties props = new Properties();    
        props.put(IConstants.MAIL_SMTP_HOST, mailSmtpHost);    
        props.put(IConstants.SOCKET_FACTORY_PORT, socketFactoryPort);    
        props.put(IConstants.SOCKET_FACTORY_CLASS, socketFactoryClass);    
        props.put(IConstants.SMTP_AUTH, smtpAuth);    
        props.put(IConstants.SMTP_PORT, smtpPort);    
        //get Session   
        Session session = Session.getDefaultInstance(props,    
         new javax.mail.Authenticator() {    
         protected PasswordAuthentication getPasswordAuthentication() {    
         return new PasswordAuthentication(fROM,pASSWORD);  
         }    
        });  
		
		try {
			MimeMessage message = new MimeMessage(session);    
	           message.addRecipient(Message.RecipientType.TO,new InternetAddress(To));    
	           message.setSubject(subject);
	           
	           MimeMultipart multiPart = new MimeMultipart("related");
	           BodyPart messageBody = new MimeBodyPart();
	           messageBody.setContent(body, "text/html");
	           multiPart.addBodyPart(messageBody);
	           message.setContent(multiPart);
	           
	           //send message  
	           Transport.send(message);    
	           System.out.println("message sent successfully");   
		} catch (Exception e) {
			throw e;
			// TODO: handle exception
		}
		
	}
	
}
