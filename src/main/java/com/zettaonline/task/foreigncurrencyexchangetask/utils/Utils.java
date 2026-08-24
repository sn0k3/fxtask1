package com.zettaonline.task.foreigncurrencyexchangetask.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class Utils {
	public static final String FX_RATE_PROVIDER_FRANKFURTER_URL = "https://api.frankfurter.dev/v1/latest";
	
	public static final String FX_RATE_PROVIDER_ERAPI_URL = "https://open.er-api.com/v6/latest/";
	
	/**
	 * Performs GET request to a given URL.
	 * @param url
	 * @return Raw response as String
	 */
	public String createExternalGETRequestToSpecificURL(String url) {
		RestTemplate restTemplate = new RestTemplate();
		
		
		ResponseEntity<String> result;
		
		try {
			result = restTemplate.getForEntity(url, String.class);

			return result.getBody();
		} catch(HttpClientErrorException.NotFound htcee) {
			return null;
		}
		
	}
	
	

}
