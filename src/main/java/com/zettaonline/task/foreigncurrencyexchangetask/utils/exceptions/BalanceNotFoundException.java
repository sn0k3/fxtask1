package com.zettaonline.task.foreigncurrencyexchangetask.utils.exceptions;

public class BalanceNotFoundException extends RuntimeException {

    public BalanceNotFoundException(String currency) {
        super("Balance not found for currency: " + currency);
    }
}