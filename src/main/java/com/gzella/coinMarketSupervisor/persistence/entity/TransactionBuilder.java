package com.gzella.coinMarketSupervisor.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionBuilder {
    private String userCoin;
    private BigDecimal ucPrice;
    private BigDecimal amountUcToExchange;
    private String coinToExchange;
    private BigDecimal ctePrice;
    private BigDecimal amountCteAfterExchange;
    private LocalDateTime timeStamp;
    private Long transactionId;
    private User user;

    public TransactionBuilder setUserCoin(String userCoin) {
        this.userCoin = userCoin;
        return this;
    }

    public TransactionBuilder setUcPrice(BigDecimal ucPrice) {
        this.ucPrice = ucPrice;
        return this;
    }

    public TransactionBuilder setAmountUcToExchange(BigDecimal amountUcToExchange) {
        this.amountUcToExchange = amountUcToExchange;
        return this;
    }

    public TransactionBuilder setCoinToExchange(String coinToExchange) {
        this.coinToExchange = coinToExchange;
        return this;
    }

    public TransactionBuilder setCtePrice(BigDecimal ctePrice) {
        this.ctePrice = ctePrice;
        return this;
    }

    public TransactionBuilder setAmountCteAfterExchange(BigDecimal amountCteAfterExchange) {
        this.amountCteAfterExchange = amountCteAfterExchange;
        return this;
    }

    public TransactionBuilder setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
        return this;
    }

    public TransactionBuilder setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    public TransactionBuilder setUser(User user) {
        this.user = user;
        return this;
    }

    public Transaction createTransaction() {
        return new Transaction(
                transactionId,
                userCoin,
                ucPrice,
                amountUcToExchange,
                coinToExchange,
                ctePrice,
                amountCteAfterExchange,
                timeStamp,
                user
        );
    }
}