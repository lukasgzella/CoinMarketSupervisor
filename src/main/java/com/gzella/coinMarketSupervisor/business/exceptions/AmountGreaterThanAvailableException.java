package com.gzella.coinMarketSupervisor.business.exceptions;

public class AmountGreaterThanAvailableException extends RuntimeException {
    public AmountGreaterThanAvailableException(String message) {
        super(message);
    }
}
