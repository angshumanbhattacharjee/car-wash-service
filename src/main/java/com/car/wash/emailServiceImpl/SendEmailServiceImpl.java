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

import com.car.wash.constants.IConstants;
import com.car.wash.emailService.SendEmailService;
import com.car.wash.model.CarWashModel;

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

	@Override
	public void sendMailToAdmin(CarWashModel model) {
		// TODO Auto-generated method stub
		
	}

}
