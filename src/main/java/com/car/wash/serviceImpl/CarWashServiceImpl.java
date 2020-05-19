
package com.car.wash.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.car.wash.constants.IConstants;
import com.car.wash.emailService.EmailService;
import com.car.wash.facade.UserFacade;
import com.car.wash.model.CarWashModel;
import com.car.wash.repository.CarWashRepository;
import com.car.wash.service.CarWashService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.profile.utility.CommonUtility;

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
	private CarWashRepository repository;

	@Autowired
	private EmailService sendMail;

	@Override
	public List<String> washCar(CarWashModel model) throws Exception {
		List<String> getWashers = null;
		try {
			saveWashDetails(model);
			getWashers = getWasherDetails(getWasherDataMap(model));
			sendMail.sendEmailToWashers(getWashers);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return getWashers;
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
			// TODO: handle exception
		}
		// TODO Auto-generated method stub
		return response;
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
		CarWashModel model1 = null;
		try {
			model.setWasherWashStatus(IConstants.APPROVE);
			model.setCarWashStatus(IConstants.INPROGRESS);
			model.setCarWashDate(CommonUtility.getCurrentDateInString());
			model.setWasherNotificationStatus(IConstants.ENABLED);
			model1 = repository.save(model);
		} catch (Exception e) {
			throw e;
			// TODO: handle exception
		}
		return model1;
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
	private List<String> getWasherDetails(Map<String, Object> washerDataMap) throws Exception {
		List<String> washerList = null;
		try {
			washerList = (List<String>) userFacade.process(mapper.writeValueAsString(washerDataMap));
			return washerList;
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
