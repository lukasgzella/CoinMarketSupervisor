package com.gzella.coinMarketSupervisor.business.exceptions;

public class NoSuchUserException extends RuntimeException {
    public NoSuchUserException() {
        super("There is no such user in database.");
    }
}
