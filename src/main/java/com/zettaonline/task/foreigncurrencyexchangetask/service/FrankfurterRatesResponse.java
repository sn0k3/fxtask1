package com.zettaonline.task.foreigncurrencyexchangetask.service;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Map;

public record FrankfurterRatesResponse(BigDecimal amount, String base, String date, Map<String, BigDecimal> rates) {
	
	
}