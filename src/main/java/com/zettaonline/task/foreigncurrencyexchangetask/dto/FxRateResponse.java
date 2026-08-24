package com.zettaonline.task.foreigncurrencyexchangetask.dto;

import java.math.BigDecimal;

public record FxRateResponse (String from, String to, BigDecimal rate) {
	
}
