/**
 * 
 */
package com.car.wash.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ANGSHUMAN
 *
 */
@Document(collection = "car-wash-service")
@Component
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = Include.NON_NULL)
@ToString
public class CarWashModel implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Getter
	@Setter
	String washingId;
	
	@Getter
	@Setter
	String customerId;
	
	@Getter
	@Setter
	String washerId;
	
	@Getter
	@Setter
	String carId;
	
	@Getter
	@Setter
	String washPackage;
	
	@Getter
	@Setter
	String extraWashAddOns;
	
	@Getter
	@Setter
	String infoNotes;
	
	@Getter
	@Setter
	String scheduledWash;
	
	@Getter
	@Setter
	String scheduledWashDate;
	
	@Getter
	@Setter
	String scheduledWashTime;
	
	@Getter
	@Setter
	String scheduledWashLocation;
	
	@Getter
	@Setter
	String washerWashStatus;
	
	@Getter
	@Setter
	String carWashStatus;
	
	@Getter
	@Setter
	String carWashDate;
	
	@Getter
	@Setter
	String washRequestDate;
	
	@Getter
	@Setter
	String washerNotificationStatus;

}
