
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
import com.car.wash.emailService.SendEmailService;
import com.car.wash.facade.CarFacade;
import com.car.wash.facade.UserFacade;
import com.car.wash.model.CarWashModel;
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
	@Qualifier("carFacade")
	private CarFacade carFacade;

	@Autowired
	private CarWashRepository repository;

	@Autowired
	private SendEmailService sendMail;

	@Override
	public List<String> washCar(CarWashModel model) throws Exception {
		List<String> getWasherEmailId = null;
		Map<String, Object> customerName = new HashMap<>();
		Map<String, Object> carName = new HashMap<>();;
		try {
			saveWashDetails(model);
			getWasherEmailId = getWasherEmailDetails(getWasherDataMap(model));
			customerName = getCustomerName(getCustomerNameMap(model));
			carName = getCarName(getCarNameMap(model));
			sendMail.sendEmailToWashers(getWasherEmailId, model, customerName.get(IConstants.USERNAME), carName.get(IConstants.CAR_MANUFACTURER_NAME));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getWasherEmailId;
	}

	@Override
	public String washerResponse(CarWashModel model) throws Exception {
		Map<String, Object> getWasherById = null;
		String response = null;
		try {
			Optional<CarWashModel> model1 = repository.findById(model.getWashingId());
			if (model.getWasherWashStatus().equals(IConstants.APPROVE)) {
				if (model1.get().getWasherNotificationStatus().equals(IConstants.ENABLED)) {
					updateWasherResponse(model);
					getWasherById = getWasherName(getWasherNameMap(model));
					response = getWasherById.get(IConstants.USERNAME) + IConstants.WASH_REQUEST_ACCEPTED;
					System.out.println(response);
				} else {
					response = IConstants.WASH_REQUEST_ALREADY_ACCEPTED;
				}
			}
			if (model.getWasherWashStatus().equals(IConstants.DECLINE)) {
				sendMail.sendMailToAdmin(model);
			}
		} catch (Exception e) {
			throw e;
		}
		return response;
	}
	
	@Override
	public String startWash(CarWashModel model) throws Exception {
		Map<String, Object> getWasherById = null;
		Map<String, Object> getCustomerById = null;
		String response = null;
		try {
			Optional<CarWashModel> model1 = repository.findById(model.getWashingId());
			if(model1.isPresent() && !StringUtils.isEmpty(model1.get().getWasherId()) && !StringUtils.isEmpty(model1.get().getCustomerId())) {
				updateStartWashStatus(model1.get());
				getWasherById = getWasherName(getWasherNameMap(model));
				getCustomerById = getCustomerName(getCustomerNameMap(model));
				response = IConstants.HI + getCustomerById.get(IConstants.USERNAME) + IConstants.EXCLAMATION + getWasherById.get(IConstants.USERNAME) + IConstants.START_WASH_RESPONSE;
			}
		} catch (Exception e) {
			throw e;
		}
		return response;
	}

	@Override
	public String endWash(CarWashModel model) throws Exception {
		Map<String, Object> getWasherById = null;
		Map<String, Object> getCustomerById = null;
		String response = null;
		try {
			Optional<CarWashModel> model1 = repository.findById(model.getWashingId());
			if(model1.isPresent() && !StringUtils.isEmpty(model1.get().getWasherId()) && !StringUtils.isEmpty(model1.get().getCustomerId())) {
				updateEndWashStatus(model1.get());
				getWasherById = getWasherName(getWasherNameMap(model));
				getCustomerById = getCustomerName(getCustomerNameMap(model));
				response = IConstants.HI + getCustomerById.get(IConstants.USERNAME) + IConstants.EXCLAMATION + getWasherById.get(IConstants.USERNAME) + IConstants.END_WASH_RESPONSE;
			}
		} catch (Exception e) {
			throw e;
		}
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

	private Map<String, Object> getCustomerNameMap(CarWashModel model) throws Exception {
		Map<String, Object> requestMap = new HashMap<>();
		try {
			requestMap.put(IConstants.USERID, model.getCustomerId());
		} catch (Exception e) {
			throw e;
		}
		return requestMap;
	}

	private Map<String, Object> getWasherNameMap(CarWashModel model) throws Exception {
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
			model1.get().setWasherWashStatus(IConstants.APPROVE);
			model1.get().setWasherId(model.getWasherId());
			model1.get().setCarWashDate(CommonUtility.getCurrentDateInString());
			model1.get().setWasherNotificationStatus(IConstants.DISABLED);
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
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, Object> getCarName(Map<String, Object> carNameMap) {
		Map<String, Object> carName = null;
		try {
			carName = (Map<String, Object>) carFacade.process(mapper.writeValueAsString(carNameMap));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return carName;
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, Object> getCustomerName(Map<String, Object> customerNameMap) throws Exception {
		Map<String, Object> customerList = null;
		try {
			customerList = (Map<String, Object>) customerFacade.process(mapper.writeValueAsString(customerNameMap));
		} catch (Exception e) {
			throw e;
		}
		return customerList;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> getWasherName(Map<String, Object> washerNameMap) throws Exception {
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

	private Map<String, Object> getWasherDataMap(CarWashModel model) throws Exception {
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
