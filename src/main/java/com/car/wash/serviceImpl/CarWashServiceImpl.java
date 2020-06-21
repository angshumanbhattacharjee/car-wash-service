
package com.car.wash.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.car.wash.constants.IConstants;
import com.car.wash.dto.UserReviewDTO;
import com.car.wash.dto.UserWashCountUpdateDTO;
import com.car.wash.emailService.SendEmailService;
import com.car.wash.facade.CarFacade;
import com.car.wash.facade.UserFacade;
import com.car.wash.facadeImpl.UserWashCountUpdateFacadeImpl;
import com.car.wash.model.CarWashModel;
import com.car.wash.publisher.RabbitMQPublish;
import com.car.wash.repository.CarWashRepository;
import com.car.wash.service.CarWashService;
import com.car.wash.utility.CommonUtility;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author ANGSHUMAN
 *
 */
@Service
public class CarWashServiceImpl implements CarWashService {

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	@Qualifier("userFacade")
	private UserFacade userFacade;

	@Autowired
	@Qualifier("washerFacade")
	private UserFacade washerFacade;
	
	@Autowired
	@Qualifier("customerFacade")
	private UserFacade customerFacade;
	
	@Autowired
	@Qualifier("adminFacade")
	private UserFacade adminFacade;
	
	@Autowired
	@Qualifier("userWashCountUpdate")
	private UserWashCountUpdateFacadeImpl userWashCountUpdate;
	
	@Autowired
	@Qualifier("carFacade")
	private CarFacade carFacade;

	@Autowired
	private CarWashRepository repository;

	@Autowired
	private SendEmailService sendMail;
	
	@Autowired
	private UserWashCountUpdateDTO washCount;
	
	@Autowired
	private RabbitMQPublish rabbit;

	@Override
	public List<String> washCar(CarWashModel model) throws Exception {
		List<String> getWasherEmailId = null;
		Map<String, Object> customerName = new HashMap<>();
		Map<String, Object> carName = new HashMap<>();;
		try {
			saveWashDetails(model);
			getWasherEmailId = getWasherEmailDetails(getWasherDataMap());
			customerName = getCustomerDetails(getCustomerDetailsMap(model));
			carName = getCarName(getCarNameMap(model));
			sendMail.sendEmailToWashers(getWasherEmailId, model, customerName.get(IConstants.USERNAME), carName.get(IConstants.CAR_MANUFACTURER_NAME));
		} catch (Exception e) {
			throw e;
		}
		return getWasherEmailId;
	}

	@Override
	public String washerResponse(CarWashModel model) throws Exception {
		Map<String, Object> washerDetails = null;
		Map<String, Object> customerDetails = null;
		Map<String, Object> carName = null;
		List<String> adminEmailId = null;
		String response = null;
		try {
			Optional<CarWashModel> model1 = repository.findById(model.getWashingId());
			customerDetails = getCustomerDetails(getCustomerDetailsMap(model1.get()));
			carName = getCarName(getCarNameMap(model1.get()));
			washerDetails = getWasherDetails(getWasherDetailsMap(model));
			if (model.getWasherWashStatus().equals(IConstants.APPROVE)) {
				if (model1.get().getWasherNotificationStatus().equals(IConstants.ENABLED)) {
					updateWasherResponse(model);
					sendMail.sendWasherResponseToCustomer(customerDetails.get(IConstants.USER_EMAIL_ID), customerDetails.get(IConstants.USERNAME), washerDetails.get(IConstants.USERNAME), carName.get(IConstants.CAR_MODEL_NAME), model1.get());
					sendMail.sendWashDetailsToWasher(customerDetails.get(IConstants.USERNAME), washerDetails.get(IConstants.USERNAME),washerDetails.get(IConstants.USER_EMAIL_ID), carName.get(IConstants.CAR_MODEL_NAME), model1.get());
					response = washerDetails.get(IConstants.USERNAME) + IConstants.WASH_REQUEST_ACCEPTED;
					System.out.println(response);
				} else {
					response = IConstants.WASH_REQUEST_ALREADY_ACCEPTED;
				}
			}
			if (model.getWasherWashStatus().equals(IConstants.DECLINE)) {
				adminEmailId = getAdminDetails(getAdminNameMap());
				updateWasherResponse(model);
				response = "Hi " + washerDetails.get(IConstants.USERNAME) + ", You have rejected the wash request!!";
				sendMail.sendMailToAdmin(customerDetails.get(IConstants.USERNAME), washerDetails.get(IConstants.USERNAME), adminEmailId, carName.get(IConstants.CAR_MODEL_NAME), model1.get());
			}
		} catch (Exception e) {
			throw e;
		}
		return response;
	}
	
	@Override
	public String startWash(CarWashModel model) throws Exception {
		Map<String, Object> washerDetails = null;
		Map<String, Object> customerDetails = null;
		Map<String, Object> carName = null;
		String response = null;
		try {
			Optional<CarWashModel> model1 = repository.findById(model.getWashingId());
			if(model1.isPresent() && !StringUtils.isEmpty(model1.get().getWasherId())) {
				updateStartWashStatus(model1.get());
				washerDetails = getWasherDetails(getWasherDetailsMap(model));
				customerDetails = getCustomerDetails(getCustomerDetailsMap(model1.get()));
				carName = getCarName(getCarNameMap(model1.get()));
				sendMail.sendStartWashStatusToCustomer(washerDetails.get(IConstants.USERNAME), customerDetails.get(IConstants.USERNAME), customerDetails.get(IConstants.USER_EMAIL_ID), carName.get(IConstants.CAR_MODEL_NAME), model1.get());
				response = IConstants.HI + customerDetails.get(IConstants.USERNAME) + IConstants.EXCLAMATION + washerDetails.get(IConstants.USERNAME) + IConstants.START_WASH_RESPONSE;
			}
		} catch (Exception e) {
			throw e;
		}
		return response;
	}

	@Override
	public String endWash(CarWashModel model) throws Exception {
		Map<String, Object> washerDetails = null;
		Map<String, Object> customerDetails = null;
		Map<String, Object> carName = null;
		String response = null;
		try {
			Optional<CarWashModel> model1 = repository.findById(model.getWashingId());
			if(model1.isPresent() && !StringUtils.isEmpty(model1.get().getWasherId())) {
				updateEndWashStatus(model1.get());
				washerDetails = getWasherDetails(getWasherDetailsMap(model));
				customerDetails = getCustomerDetails(getCustomerDetailsMap(model1.get()));
				carName = getCarName(getCarNameMap(model1.get()));
				sendMail.sendEndWashStatusToCustomer(customerDetails.get(IConstants.USERNAME), customerDetails.get(IConstants.USER_EMAIL_ID), washerDetails.get(IConstants.USERNAME), carName.get(IConstants.CAR_MODEL_NAME), model1.get());
				sendMail.sendEndWashStatusToWasher(washerDetails.get(IConstants.USERNAME), washerDetails.get(IConstants.USER_EMAIL_ID), customerDetails.get(IConstants.USERNAME), carName.get(IConstants.CAR_MODEL_NAME), model1.get());
				washCount.setUserIdCustomer(model1.get().getCustomerId());
				washCount.setUserIdWasher(model1.get().getWasherId());
				rabbit.publishUserWashCountDTO(washCount);
				response = washerDetails.get(IConstants.USERNAME) + IConstants.END_WASH_RESPONSE + CommonUtility.getCurrentDateInString();
			}
		} catch (Exception e) {
			throw e;
		}
		return response;
	}
	
	@Override
	public String provideUserReview(UserReviewDTO userReviewDTO) {
		String response = null;
		rabbit.publishUserReviewDTO(userReviewDTO);
		response = IConstants.REVIEW;
		return response;
	}
	
	private void updateStartWashStatus(CarWashModel carWashModel) {
		try {
			carWashModel.setCarWashStatus(IConstants.INPROGRESS);
			repository.save(carWashModel);
		} catch (Exception e) {
			throw e;
		}
		
	}
	
	private void updateEndWashStatus(CarWashModel carWashModel) {
		try {
			carWashModel.setCarWashStatus(IConstants.COMPLETE);
			repository.save(carWashModel);
		} catch (Exception e) {
			throw e;
		}
		
	}
	
	private Map<String, Object> getCarNameMap(CarWashModel model) {
		Map<String, Object> requestMap = new HashMap<>();
		try {
			requestMap.put(IConstants.CARID, model.getCarId());
		} catch (Exception e) {
			throw e;
		}
		return requestMap;
	}

	private Map<String, Object> getCustomerDetailsMap(CarWashModel model) throws Exception {
		Map<String, Object> requestMap = new HashMap<>();
		try {
			requestMap.put(IConstants.USERID, model.getCustomerId());
		} catch (Exception e) {
			throw e;
		}
		return requestMap;
	}
	
	private Map<String, Object> getAdminNameMap() throws Exception {
		Map<String, Object> requestMap = new HashMap<>();
		try {
			requestMap.put(IConstants.USERROLE, IConstants.ADMIN);
		} catch (Exception e) {
			throw e;
		}
		return requestMap;
	}

	private Map<String, Object> getWasherDetailsMap(CarWashModel model) throws Exception {
		Map<String, Object> requestMap = new HashMap<>();
		try {
			requestMap.put(IConstants.USERID, model.getWasherId());
		} catch (Exception e) {
			throw e;
			// TODO: handle exception
		}
		return requestMap;
	}

	private CarWashModel updateWasherResponse(CarWashModel model) {
		Optional<CarWashModel> model1 = null;
		try {
			model1 = repository.findById(model.getWashingId());
			if(model.getWasherWashStatus().equals(IConstants.APPROVE)) {
				model1.get().setWasherWashStatus(IConstants.APPROVE);
				model1.get().setWasherId(model.getWasherId());
				model1.get().setCarWashDate(CommonUtility.getCurrentDateInString());
				model1.get().setWasherNotificationStatus(IConstants.DISABLED);
			}
			if(model.getWasherWashStatus().equals(IConstants.DECLINE)) {
				model1.get().setWasherWashStatus(IConstants.DECLINE);
				model1.get().setWasherId(model.getWasherId());
			}
			return repository.save(model1.get());
		} catch (Exception e) {
			throw e;
			// TODO: handle exception
		}
	}

	private CarWashModel saveWashDetails(CarWashModel model) {

		try {
			model.setCarWashStatus(IConstants.PENDING);
			model.setWasherWashStatus(IConstants.PENDING);
			model.setWashRequestDate(CommonUtility.getCurrentDateInString());
			model.setWasherNotificationStatus(IConstants.ENABLED);
			return repository.save(model);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, Object> getCarName(Map<String, Object> carNameMap) throws Exception {
		Map<String, Object> carName = null;
		try {
			carName = (Map<String, Object>) carFacade.process(mapper.writeValueAsString(carNameMap));
		} catch (Exception e) {
			throw e;
			// TODO: handle exception
		}
		return carName;
	}
	
	@SuppressWarnings("unchecked")
	private List<String> getAdminDetails(Map<String, Object> adminNameMap) throws Exception {
		List<String> adminMap = null;
		try {
			adminMap = (List<String>) adminFacade.process(mapper.writeValueAsString(adminNameMap));
		} catch (Exception e) {
			throw e;
		}
		return adminMap;
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, Object> getCustomerDetails(Map<String, Object> customerNameMap) throws Exception {
		Map<String, Object> customerList = null;
		try {
			customerList = (Map<String, Object>) customerFacade.process(mapper.writeValueAsString(customerNameMap));
		} catch (Exception e) {
			throw e;
		}
		return customerList;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> getWasherDetails(Map<String, Object> washerNameMap) throws Exception {
		Map<String, Object> washerList = null;
		try {
			washerList = (Map<String, Object>) washerFacade.process(mapper.writeValueAsString(washerNameMap));
		} catch (Exception e) {
			throw e;
			// TODO: handle exception
		}
		return washerList;
	}

	@SuppressWarnings("unchecked")
	private List<String> getWasherEmailDetails(Map<String, Object> washerDataMap) throws Exception {
		List<String> washerEmailList = null;
		try {
			washerEmailList = (List<String>) userFacade.process(mapper.writeValueAsString(washerDataMap));
			return washerEmailList;
		} catch (Exception e) {
			throw e;
			// TODO: handle exception
		}
	}

	private Map<String, Object> getWasherDataMap() throws Exception {
		Map<String, Object> requestMap = new HashMap<>();
		try {
			requestMap.put(IConstants.USERROLE, IConstants.WASHER);
		} catch (Exception e) {
			throw e;
		}
		// TODO Auto-generated method stub
		return requestMap;
	}


}
