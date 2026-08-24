package com.zettaonline.task.foreigncurrencyexchangetask.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "conversions")
public class Conversion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
        name = "transaction_id",
        nullable = false,
        unique = true
    )
    private UUID transactionId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(
        name = "source_amount",
        nullable = false,
        precision = 19,
        scale = 4
    )
    private BigDecimal sourceAmount;

    @Column(
        name = "source_currency",
        nullable = false,
        length = 3
    )
    private String sourceCurrency;

    @Column(
        name = "target_amount",
        nullable = false,
        precision = 19,
        scale = 4
    )
    private BigDecimal targetAmount;

    @Column(
        name = "target_currency",
        nullable = false,
        length = 3
    )
    private String targetCurrency;

    @Column(
        name = "rate",
        nullable = false,
        precision = 19,
        scale = 10
    )
    private BigDecimal rate;

    @Column(
        name = "idempotency_key",
        nullable = false,
        length = 255
    )
    private String idempotencyKey;

    @Column(
        name = "created_at",
        nullable = false,
        updatable = false
    )
    private Instant createdAt;

    protected Conversion() {
        // Required by JPA
    }

    public Conversion(
        Client client,
        BigDecimal sourceAmount,
        String sourceCurrency,
        BigDecimal targetAmount,
        String targetCurrency,
        BigDecimal rate,
        String idempotencyKey
    ) {
        this.client = client;
        this.sourceAmount = sourceAmount;
        this.sourceCurrency = sourceCurrency;
        this.targetAmount = targetAmount;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
        this.idempotencyKey = idempotencyKey;
    }

    @PrePersist
    protected void onCreate() {
        if (transactionId == null) {
            transactionId = UUID.randomUUID();
        }

        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }

    public Long getId() {
        return id;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public Client getClient() {
        return client;
    }

    public BigDecimal getSourceAmount() {
        return sourceAmount;
    }

    public String getSourceCurrency() {
        return sourceCurrency;
    }

    public BigDecimal getTargetAmount() {
        return targetAmount;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}