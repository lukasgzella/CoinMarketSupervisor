package com.gzella.coinMarketSupervisor.business.dto;

import java.math.BigDecimal;

public class TransactionDTOBuilder {
    private String userCoin;
    private BigDecimal ucPrice;
    private BigDecimal amountUcToExchange;
    private String coinToExchange;
    private BigDecimal ctePrice;
    private BigDecimal amountCteAfterExchange;
    private String timeStamp;

    public TransactionDTOBuilder setUserCoin(String userCoin) {
        this.userCoin = userCoin;
        return this;
    }

    public TransactionDTOBuilder setUcPrice(BigDecimal ucPrice) {
        this.ucPrice = ucPrice;
        return this;
    }

    public TransactionDTOBuilder setAmountUcToExchange(BigDecimal amountUcToExchange) {
        this.amountUcToExchange = amountUcToExchange;
        return this;
    }

    public TransactionDTOBuilder setCoinToExchange(String coinToExchange) {
        this.coinToExchange = coinToExchange;
        return this;
    }

    public TransactionDTOBuilder setCtePrice(BigDecimal ctePrice) {
        this.ctePrice = ctePrice;
        return this;
    }

    public TransactionDTOBuilder setAmountCteAfterExchange(BigDecimal amountCteAfterExchange) {
        this.amountCteAfterExchange = amountCteAfterExchange;
        return this;
    }

    public TransactionDTOBuilder setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
        return this;
    }

    public TransactionDTO createTransactionDTO() {
        return new TransactionDTO(
                userCoin,
                ucPrice,
                amountUcToExchange,
                coinToExchange,
                ctePrice,
                amountCteAfterExchange,
                timeStamp
        );
    }
}