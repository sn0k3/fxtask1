package com.zettaonline.task.foreigncurrencyexchangetask.repository;

import com.zettaonline.task.foreigncurrencyexchangetask.entities.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;

import java.util.List;
import java.util.Optional;

public interface BalanceRepository extends JpaRepository<Balance, Long> {

	Optional<Balance> findByClient_IdAndCurrency(Long clientId, String currency);

	/**
	 * Locks all balances belonging to a client.
	 *
	 * PESSIMISTIC_WRITE prevents another conversion for the same client from
	 * modifying the balances concurrently.
	 */
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("""
			SELECT b
			FROM Balance b
			WHERE b.client.id = :clientId
			ORDER BY b.currency ASC
			""")
	List<Balance> findAllByClientIdForUpdate(@Param("clientId") Long clientId);

	@Query("""
			SELECT b
			FROM Balance b
			WHERE b.client.id = :clientId
			ORDER BY b.currency ASC
			""")
	List<Balance> findAllByClientIdOrderByCurrency(@Param("clientId") Long clientId);
}