package com.zettaonline.task.foreigncurrencyexchangetask.repository;

import java.util.List;


import com.zettaonline.task.foreigncurrencyexchangetask.entities.Client;
import org.springframework.stereotype.Service;


@Service
public class ClientService {
	
	private ClientRepository clientRepository;
	
	public ClientService(ClientRepository clientRepository) {
		this.clientRepository = clientRepository;
	}
	
	public Client getSpecificClient(String clientId) {
		return clientRepository.findByClientIdentification(clientId).get();
	}
	
	public List<Client> getClients() {
		return clientRepository.findAll();
	}
}