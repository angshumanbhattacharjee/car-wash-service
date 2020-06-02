/**
 * 
 */
package com.car.wash.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.car.wash.constants.IConstants;
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
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping(value = "/car-wash-service/washCar" , produces = "application/json")
	ResponseEntity washCar (@RequestBody(required = true) CarWashModel model) throws Exception {
		ResponseEntity response = null;
		try {
			response = new ResponseEntity(service.washCar(model) , HttpStatus.OK);
		} catch (Exception e) {
			response = new ResponseEntity(IConstants.ERROR_IN_PROCESSING_REQUEST , HttpStatus.BAD_REQUEST);
			System.out.println("Error Message :" + e.getMessage());
			System.out.println("Error Message :" + e.getStackTrace());
			throw e;
		}
		return response ;	
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping(value = "/car-wash-service/washerResponse" , produces = MediaType.TEXT_PLAIN_VALUE)
	ResponseEntity washerResponse(@RequestBody(required = true) CarWashModel model) throws Exception {
		ResponseEntity response = null;
		try {
			response = new ResponseEntity(service.washerResponse(model), HttpStatus.OK);
		} catch (Exception e) {
			response = new ResponseEntity(IConstants.ERROR_IN_PROCESSING_REQUEST , HttpStatus.BAD_REQUEST);
			throw e;
			// TODO: handle exception
		}
		return response ;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping(value = "/car-wash-service/startCarWash" , produces = MediaType.TEXT_PLAIN_VALUE)
	ResponseEntity startCarWash(@RequestBody(required = true) CarWashModel model) throws Exception {
		ResponseEntity response = null;
		try {
			response = new ResponseEntity(service.startWash(model), HttpStatus.OK);
		} catch (Exception e) {
			response = new ResponseEntity(IConstants.ERROR_IN_PROCESSING_REQUEST , HttpStatus.BAD_REQUEST);
		}
		return response ;
		
	}

}
