package com.gzella.coinMarketSupervisor.persistence.entity;

import java.math.BigDecimal;

public class WalletEntryBuilder {
    private String coinSymbol;
    private User user;
    private BigDecimal amount;
    private Long walletEntryId;

    public WalletEntryBuilder setCoinSymbol(String coinSymbol) {
        this.coinSymbol = coinSymbol;
        return this;
    }

    public WalletEntryBuilder setUser(User user) {
        this.user = user;
        return this;
    }

    public WalletEntryBuilder setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public WalletEntryBuilder setWalletEntryId(Long walletEntryId) {
        this.walletEntryId = walletEntryId;
        return this;
    }

    public WalletEntry createWalletEntry() {
        return new WalletEntry(coinSymbol, user, amount);
    }
}