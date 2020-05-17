/**
 * 
 */
package com.car.wash.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.car.wash.model.CarWashModel;
import com.car.wash.service.CarWashService;

/**
 * @author ANGSHUMAN
 *
 */
@RestController
public class CarWashController {
	
	@Autowired
	private CarWashService service;
	
	@PostMapping(value = "/car-wash-service/washCar" , produces = "application/json")
	ResponseEntity washCar (@RequestBody(required = true) CarWashModel model) throws Exception {
		ResponseEntity response = null;
		try {
			response = new ResponseEntity(service.washCar(model) , HttpStatus.OK);
		} catch (Exception e) {
			response = new ResponseEntity("Could not process your request at this moment!!!" , HttpStatus.BAD_REQUEST);
			System.out.println("Error Message :" + e.getMessage());
			System.out.println("Error Message :" + e.getStackTrace());
			throw e;
		}
		return response ;	
	}

}
