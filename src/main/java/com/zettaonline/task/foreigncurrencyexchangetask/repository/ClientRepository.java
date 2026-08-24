package com.zettaonline.task.foreigncurrencyexchangetask.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zettaonline.task.foreigncurrencyexchangetask.entities.Client;


public interface ClientRepository extends JpaRepository<Client, Long> {

	Optional<Client> findByClientIdentification(String clientId);
	
}
