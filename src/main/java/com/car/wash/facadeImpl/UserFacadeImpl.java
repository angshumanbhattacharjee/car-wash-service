/**
 * 
 */
package com.car.wash.facadeImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.car.wash.facade.UserFacade;

/**
 * @author ANGSHUMAN
 *
 */
@Component
public class UserFacadeImpl implements UserFacade{
	
	@Value("${user.profile.service.criteria.uri}")
	private String userServiceUri;
	
	@Autowired
	private RestTemplate restTemplate;

	@Override
	public List<String> process(Object obj) throws Exception{
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<>(obj.toString(), header);
		List<String> response = null;
		try {
			response = restTemplate.exchange(userServiceUri, HttpMethod.POST, request, List.class).getBody();
		} catch (Exception e) {
			// TODO: handle exception
		}
		// TODO Auto-generated method stub
		return response;
	}

}
