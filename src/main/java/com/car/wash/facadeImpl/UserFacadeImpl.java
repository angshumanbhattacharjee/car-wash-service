/**
 * 
 */
package com.car.wash.facadeImpl;

import java.awt.List;
import java.util.HashMap;
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
	public Map<String, Object> process(Object obj) throws Exception{
		Map<String, Object> map = new HashMap<>();
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<>(obj.toString(), header);
		try {
			List<Map<String, Object>> response = restTemplate.exchange(userServiceUri, HttpMethod.POST, request, List.class).getBody();
		} catch (Exception e) {
			// TODO: handle exception
		}
		// TODO Auto-generated method stub
		return map;
	}

}
