package com.gzella.coinMarketSupervisor.business.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Incorrect transaction object received from browser")
public class IncorrectTransactionDetailsException extends RuntimeException {
}
