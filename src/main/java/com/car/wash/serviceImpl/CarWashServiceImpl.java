/**
 * 
 */
package com.car.wash.serviceImpl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.car.wash.facade.UserFacade;
import com.car.wash.model.CarWashModel;
import com.car.wash.service.CarWashService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author ANGSHUMAN
 *
 */
@Service
public class CarWashServiceImpl implements CarWashService {

	@Autowired
	private ObjectMapper mapper;
	
	private UserFacade userFacade;
	
	@Override
	public CarWashModel washCar(CarWashModel model) throws Exception {
		CarWashModel model1 = null;
		Map <String, Object> getWashers = getWasherDetails(getWasherDataMap(model)); 
		return model1 ;
	}

	private Map<String, Object> getWasherDetails(Map<String, Object> washerDataMap) {
		Map<String, Object> washerMap = new HashMap<>();
		try {
			washerMap = userFacade.process(mapper.writeValueAsString(washerDataMap));
			return washerMap;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	private Map<String , Object> getWasherDataMap(CarWashModel model) throws Exception{
		Map<String, Object> requestMap = new HashMap<>();
		try {
			requestMap.put("userRole", "washer");
		} catch (Exception e) {
			throw e;
		}
		// TODO Auto-generated method stub
		return requestMap;
	}

}
