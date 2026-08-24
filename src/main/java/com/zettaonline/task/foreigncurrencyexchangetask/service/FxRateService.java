package com.zettaonline.task.foreigncurrencyexchangetask.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zettaonline.task.foreigncurrencyexchangetask.utils.Utils;
import com.zettaonline.task.foreigncurrencyexchangetask.utils.pojos.FxRates;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Locale;

@Service
public class FxRateService {

	private final ObjectMapper objectMapper;

	public FxRateService(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	public BigDecimal getRate(String sourceCurrency, String targetCurrency) {
		String from = sourceCurrency.trim().toUpperCase(Locale.ROOT);

		String to = targetCurrency.trim().toUpperCase(Locale.ROOT);

		if (from.equals(to)) {
			return BigDecimal.ONE;
		}

		Utils utils = new Utils();

		String rawResult = utils
				.createExternalGETRequestToSpecificURL(Utils.FX_RATE_PROVIDER_FRANKFURTER_URL + "?from=" + from);

		if (rawResult == null) {
			throw new IllegalStateException("Unable to retrieve FX rates");
		}

		try {
			FxRates rates = objectMapper.readValue(rawResult, FxRates.class);

			BigDecimal rate = rates.getRates().get(to);

			if (rate == null) {
				throw new IllegalArgumentException("Unsupported target currency: " + to);
			}

			return rate;

		} catch (JsonProcessingException exception) {
			throw new IllegalStateException("Unable to parse FX provider response", exception);
		}
	}
}