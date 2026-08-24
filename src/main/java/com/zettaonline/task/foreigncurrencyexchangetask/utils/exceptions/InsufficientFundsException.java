package com.zettaonline.task.foreigncurrencyexchangetask.utils.exceptions;


public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(String currency) {
        super("Insufficient funds in " + currency + " balance");
    }
}