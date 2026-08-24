package com.zettaonline.task.foreigncurrencyexchangetask.entities;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "balances", uniqueConstraints = {
		@UniqueConstraint(name = "uk_balances_client_currency", columnNames = { "client_id", "currency" }) }
)
public class Balance {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "client_id", nullable = false)
	private Client client;

	@Column(name = "currency", nullable = false, length = 3)
	private String currency;

	@Column(name = "amount", nullable = false, precision = 19, scale = 4)
	private BigDecimal amount;

	protected Balance() {

	}

	public Balance(Client client, String currency, BigDecimal amount) {
		this.client = client;
		this.currency = currency;
		this.amount = amount;
	}

	public Long getId() {
		return id;
	}

	public Client getClient() {
		return client;
	}

	public String getCurrency() {
		return currency;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
}
