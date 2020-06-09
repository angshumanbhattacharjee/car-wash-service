package com.car.wash.facadeImpl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.car.wash.facade.UserFacade;

@Component
@Qualifier("userWashCountUpdate")
public class UserWashCountUpdateFacadeImpl implements UserFacade {

	@Value("${user.profile.service.user-wash-count-update.uri}")
	private String userServiceUri;
	
	@Autowired
	private RestTemplate restTemplate;

	@SuppressWarnings({ })
	@Override
	public String process(Object obj) throws Exception{
		String response = null;
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<>(obj.toString(), header);
		try {
			response = restTemplate.exchange(userServiceUri, HttpMethod.POST, request, String.class).getBody();
		} catch (Exception e) {
			throw e;
		}
		// TODO Auto-generated method stub
		return response;
	}

}
