package com.car.wash.dto;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Component
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserWashCountUpdateDTO implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Getter
	@Setter
	String userIdCustomer;
	
	@Getter
	@Setter
	String userIdWasher;

}
