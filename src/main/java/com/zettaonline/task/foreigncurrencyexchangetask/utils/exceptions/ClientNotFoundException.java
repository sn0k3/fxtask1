package com.zettaonline.task.foreigncurrencyexchangetask.utils.exceptions;


public class ClientNotFoundException extends RuntimeException {

    public ClientNotFoundException(String clientId) {
        super("Client not found: " + clientId);
    }
}