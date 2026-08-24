package com.zettaonline.task.foreigncurrencyexchangetask.repository;

import com.zettaonline.task.foreigncurrencyexchangetask.entities.Conversion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ConversionRepository extends JpaRepository<Conversion, Long> {

    Optional<Conversion> findByTransactionId(UUID transactionId);

    Optional<Conversion> findByClient_IdAndIdempotencyKey(
        Long clientId,
        String idempotencyKey
    );
}