/**
 * 
 */
package com.car.wash.service;

import java.util.List;

import com.car.wash.dto.UserReviewDTO;
import com.car.wash.model.CarWashModel;

/**
 * @author ANGSHUMAN
 *
 */
public interface CarWashService {

	public List<String> washCar(CarWashModel model) throws Exception;

	public String washerResponse(CarWashModel model) throws Exception;

	public String startWash(CarWashModel model) throws Exception;

	public String endWash(CarWashModel model) throws Exception;

	public String provideUserReview(UserReviewDTO userReviewDTO);

}
