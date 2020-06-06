package com.car.wash.facadeImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.car.wash.constants.IConstants;
import com.car.wash.facade.UserFacade;

@Component
@Qualifier("washerFacade")
public class WasherFacadeImpl implements UserFacade {

	@Value("${user.profile.service.criteria.uri}")
	private String userServiceUri;
	
	@Autowired
	private RestTemplate restTemplate;

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> process(Object obj) throws Exception{
		Map<String, Object> map = new HashMap<>();
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<>(obj.toString(), header);
		List<Map<String, Object>> response = null;
		try {
			response = restTemplate.exchange(userServiceUri, HttpMethod.POST, request, List.class).getBody();
			if(response != null && !response.isEmpty()) {
				for (Map<String, Object> entry : response) {
					map.put(IConstants.USERNAME, entry.get(IConstants.USERNAME));
					map.put(IConstants.USER_EMAIL_ID, entry.get(IConstants.USER_EMAIL_ID));
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
		// TODO Auto-generated method stub
		return map;
	}
}
