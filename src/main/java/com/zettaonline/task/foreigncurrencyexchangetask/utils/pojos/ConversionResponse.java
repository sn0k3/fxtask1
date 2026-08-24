package com.zettaonline.task.foreigncurrencyexchangetask.utils.pojos;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class ConversionResponse {

	private UUID transactionId;

	private BigDecimal sourceAmount;
	
	private String sourceCurrency;

	private BigDecimal targetAmount;
	
	private String targetCurrency;

	private BigDecimal rate;

	private Instant timestamp;

	private List<BalanceResponse> balances;
	
	

	public ConversionResponse() {
	}

	public ConversionResponse(UUID transactionId, BigDecimal sourceAmount, String sourceCurrency,
			BigDecimal targetAmount, String targetCurrency, BigDecimal rate, Instant timestamp,
			List<BalanceResponse> balances) {
		this.transactionId = transactionId;
		this.sourceAmount = sourceAmount;
		this.sourceCurrency = sourceCurrency;
		this.targetAmount = targetAmount;
		this.targetCurrency = targetCurrency;
		this.rate = rate;
		this.timestamp = timestamp;
		this.balances = balances;
	}

	public UUID getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(UUID transactionId) {
		this.transactionId = transactionId;
	}

	public BigDecimal getSourceAmount() {
		return sourceAmount;
	}

	public void setSourceAmount(BigDecimal sourceAmount) {
		this.sourceAmount = sourceAmount;
	}

	public String getSourceCurrency() {
		return sourceCurrency;
	}

	public void setSourceCurrency(String sourceCurrency) {
		this.sourceCurrency = sourceCurrency;
	}

	public BigDecimal getTargetAmount() {
		return targetAmount;
	}

	public void setTargetAmount(BigDecimal targetAmount) {
		this.targetAmount = targetAmount;
	}

	public String getTargetCurrency() {
		return targetCurrency;
	}

	public void setTargetCurrency(String targetCurrency) {
		this.targetCurrency = targetCurrency;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}

	public List<BalanceResponse> getBalances() {
		return balances;
	}

	public void setBalances(List<BalanceResponse> balances) {
		this.balances = balances;
	}
}