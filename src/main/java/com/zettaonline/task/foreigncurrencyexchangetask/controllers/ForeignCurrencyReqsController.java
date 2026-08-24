package com.zettaonline.task.foreigncurrencyexchangetask.controllers;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zettaonline.task.foreigncurrencyexchangetask.dto.FxRateResponse;
import com.zettaonline.task.foreigncurrencyexchangetask.entities.Client;
import com.zettaonline.task.foreigncurrencyexchangetask.repository.ClientService;
import com.zettaonline.task.foreigncurrencyexchangetask.service.BalanceService;
import com.zettaonline.task.foreigncurrencyexchangetask.service.ConversionService;
import com.zettaonline.task.foreigncurrencyexchangetask.utils.Utils;
import com.zettaonline.task.foreigncurrencyexchangetask.utils.pojos.BalanceResponse;
import com.zettaonline.task.foreigncurrencyexchangetask.utils.pojos.ConversionRequest;
import com.zettaonline.task.foreigncurrencyexchangetask.utils.pojos.ConversionResponse;
import com.zettaonline.task.foreigncurrencyexchangetask.utils.pojos.FxRates;

import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;

@Controller
@RestController
public class ForeignCurrencyReqsController {
	
	@Autowired
	private ClientService clientService;
	
	@Autowired 
	private ConversionService conversionService;

	@Autowired
	private BalanceService balanceService;

	
	// Few tests
	// http://localhost:8080/rates?from=USD&to=TRY
	// http://localhost:8080/rates?from=USD&to=JPY
	
	@GetMapping("/rates")
	public String getCurrentRates(@RequestParam(name="from") String fromCurrency, @RequestParam(name="to") String toCurrency) {
		
		String result;
		Utils utilsIns = new Utils();
		
		
		String rawResult = utilsIns.createExternalGETRequestToSpecificURL(Utils.FX_RATE_PROVIDER_FRANKFURTER_URL + "?from=" + fromCurrency);
		if(rawResult == null) {
			return "Error, invalid parameter in \"from\" field!";
		}
		
		ObjectMapper objMapper = new ObjectMapper();
		FxRates rates;

		
		try {
			rates = objMapper.readValue(rawResult, FxRates.class);
			
			BigDecimal getRatesTo = rates.getRates().get(toCurrency);
			
	
			
			if(getRatesTo == null) {
				return "Error, invalid parameter in \"to\" field!";
			}
			
			return "The exchange rate of 1 " + fromCurrency + " is equal to " + getRatesTo + " " + toCurrency;
			
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch(NullPointerException e) {
			e.printStackTrace();
		}
		

		return "Error, while returning current exchange rates!";
	}
		
	
	@PostMapping("/conversions")
	public ResponseEntity<ConversionResponse> convert(@RequestHeader("X-Client-Id") String clientId,
			@RequestHeader("Idempotency-Key") String idempotencyKey, @Valid @RequestBody ConversionRequest request) {
		ConversionResponse response = conversionService.convert(clientId, idempotencyKey, request);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	
	@GetMapping("/clients/{clientId}/balances")
	public ResponseEntity<List<BalanceResponse>> getBalances(@PathVariable String clientId) {
		return ResponseEntity.ok(balanceService.getBalances(clientId));
	}
	
}
