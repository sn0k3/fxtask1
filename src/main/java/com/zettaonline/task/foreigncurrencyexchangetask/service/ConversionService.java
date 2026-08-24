package com.zettaonline.task.foreigncurrencyexchangetask.service;

import com.zettaonline.task.foreigncurrencyexchangetask.entities.Balance;
import com.zettaonline.task.foreigncurrencyexchangetask.entities.Client;
import com.zettaonline.task.foreigncurrencyexchangetask.entities.Conversion;
import com.zettaonline.task.foreigncurrencyexchangetask.repository.BalanceRepository;
import com.zettaonline.task.foreigncurrencyexchangetask.repository.ClientRepository;
import com.zettaonline.task.foreigncurrencyexchangetask.repository.ConversionRepository;
import com.zettaonline.task.foreigncurrencyexchangetask.utils.exceptions.BalanceNotFoundException;
import com.zettaonline.task.foreigncurrencyexchangetask.utils.exceptions.ClientNotFoundException;
import com.zettaonline.task.foreigncurrencyexchangetask.utils.exceptions.InsufficientFundsException;
import com.zettaonline.task.foreigncurrencyexchangetask.utils.pojos.BalanceResponse;
import com.zettaonline.task.foreigncurrencyexchangetask.utils.pojos.ConversionRequest;
import com.zettaonline.task.foreigncurrencyexchangetask.utils.pojos.ConversionResponse;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Locale;

@Service
public class ConversionService {

	private static final int MONEY_SCALE = 4;

	private final ClientRepository clientRepository;
	private final BalanceRepository balanceRepository;
	private final ConversionRepository conversionRepository;
	private final FxRateService fxRateService;

	public ConversionService(ClientRepository clientRepository, BalanceRepository balanceRepository,
			ConversionRepository conversionRepository, FxRateService fxRateService) {
		this.clientRepository = clientRepository;
		this.balanceRepository = balanceRepository;
		this.conversionRepository = conversionRepository;
		this.fxRateService = fxRateService;
	}

	@Transactional
	public ConversionResponse convert(String clientId, String idempotencyKey, ConversionRequest request) {
		if (clientId == null || clientId.isBlank()) {
			throw new IllegalArgumentException("X-Client-Id header is required");
		}

		if (idempotencyKey == null || idempotencyKey.isBlank()) {
			throw new IllegalArgumentException("Idempotency-Key header is required");
		}

		Client client = clientRepository.findByClientIdentification(clientId)
				.orElseThrow(() -> new ClientNotFoundException(clientId));

		/*
		 * Lock all balances belonging to this client.
		 *
		 * This serializes conversions for the same client and prevents two concurrent
		 * requests from spending the same balance twice.
		 */
		List<Balance> lockedBalances = balanceRepository.findAllByClientIdForUpdate(client.getId());

		/*
		 * Check idempotency AFTER acquiring the client balance lock.
		 *
		 * This is important for concurrent requests using the same idempotency key:
		 *
		 * Request A -> gets lock -> creates conversion -> commits Request B -> waits ->
		 * gets lock -> finds conversion
		 */
		var existingConversion = conversionRepository.findByClient_IdAndIdempotencyKey(client.getId(), idempotencyKey);

		if (existingConversion.isPresent()) {
			return buildResponse(existingConversion.get(), lockedBalances);
		}

		String sourceCurrency = normalizeCurrency(request.getSourceCurrency());

		String targetCurrency = normalizeCurrency(request.getTargetCurrency());

		Balance sourceBalance = findBalance(lockedBalances, sourceCurrency);

		Balance targetBalance = findBalance(lockedBalances, targetCurrency);

		BigDecimal sourceAmount = request.getSourceAmount().setScale(MONEY_SCALE, RoundingMode.HALF_UP);

		if (sourceBalance.getAmount().compareTo(sourceAmount) < 0) {
			throw new InsufficientFundsException(sourceCurrency);
		}

		BigDecimal rate = fxRateService.getRate(sourceCurrency, targetCurrency);

		BigDecimal targetAmount = sourceAmount.multiply(rate).setScale(MONEY_SCALE, RoundingMode.HALF_UP);

		sourceBalance.setAmount(sourceBalance.getAmount().subtract(sourceAmount));

		targetBalance.setAmount(targetBalance.getAmount().add(targetAmount));

		Conversion conversion = new Conversion(client, sourceAmount, sourceCurrency, targetAmount, targetCurrency, rate,
				idempotencyKey);

		Conversion savedConversion = conversionRepository.save(conversion);

		return buildResponse(savedConversion, lockedBalances);
	}

	private Balance findBalance(List<Balance> balances, String currency) {
		return balances.stream().filter(balance -> balance.getCurrency().equals(currency)).findFirst()
				.orElseThrow(() -> new BalanceNotFoundException(currency));
	}

	private ConversionResponse buildResponse(Conversion conversion, List<Balance> balances) {
		List<BalanceResponse> balanceResponses = balances.stream()
				.map(balance -> new BalanceResponse(balance.getCurrency(), balance.getAmount())).toList();

		return new ConversionResponse(conversion.getTransactionId(), conversion.getSourceAmount(),
				conversion.getSourceCurrency(), conversion.getTargetAmount(), conversion.getTargetCurrency(),
				conversion.getRate(), conversion.getCreatedAt(), balanceResponses);
	}

	private String normalizeCurrency(String currency) {
		return currency.trim().toUpperCase(Locale.ROOT);
	}
}