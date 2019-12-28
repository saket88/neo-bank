package com.bank.services.exception;

public class CurrencyNotAllowedException extends RuntimeException {
    public CurrencyNotAllowedException(String message) {
        super(message);
    }
}
