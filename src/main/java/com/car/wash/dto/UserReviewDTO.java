/**
 * 
 */
package com.car.wash.dto;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ANGSHUMAN
 *
 */
@Component
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserReviewDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	String customerId;

	@Getter
	@Setter
	String washerId;
	
	@Getter
	@Setter
	String userReviewForCustomer;
	
	@Getter
	@Setter
	String userReviewForWasher;
	
	@Getter
	@Setter
	Double averageRatingForCustomer;
	
	@Getter
	@Setter
	Double averageRatingForWasher;

}
