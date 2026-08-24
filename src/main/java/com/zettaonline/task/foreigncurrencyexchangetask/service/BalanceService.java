package com.zettaonline.task.foreigncurrencyexchangetask.service;

import com.zettaonline.task.foreigncurrencyexchangetask.entities.Balance;
import com.zettaonline.task.foreigncurrencyexchangetask.entities.Client;
import com.zettaonline.task.foreigncurrencyexchangetask.repository.BalanceRepository;
import com.zettaonline.task.foreigncurrencyexchangetask.repository.ClientRepository;
import com.zettaonline.task.foreigncurrencyexchangetask.utils.exceptions.ClientNotFoundException;
import com.zettaonline.task.foreigncurrencyexchangetask.utils.pojos.BalanceResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BalanceService {

	private final ClientRepository clientRepository;
	private final BalanceRepository balanceRepository;

	public BalanceService(ClientRepository clientRepository, BalanceRepository balanceRepository) {
		this.clientRepository = clientRepository;
		this.balanceRepository = balanceRepository;
	}
	
	@Transactional(readOnly = true)
	public List<BalanceResponse> getBalances(String clientId) {

	    Client client = clientRepository
	        .findByClientIdentification(clientId)
	        .orElseThrow(() -> new ClientNotFoundException(clientId));

	    List<Balance> balances =
	        balanceRepository.findAllByClientIdOrderByCurrency(
	            client.getId()
	        );

	    return balances.stream()
	        .map(balance -> new BalanceResponse(
	            balance.getCurrency(),
	            balance.getAmount()
	        ))
	        .toList();
	}
}