package com.gzella.coinMarketSupervisor.persistence.entity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public class UserBuilder {
    private Long userId;
    private String username;
    private String password;
    private String role;
    private BigDecimal externalFunds;
    private List<WalletEntry> walletEntries;
    private Set<Transaction> transactions;

    public UserBuilder setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public UserBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public UserBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder setRole(String role) {
        this.role = role;
        return this;
    }

    public UserBuilder setExternalFunds(BigDecimal externalFunds) {
        this.externalFunds = externalFunds;
        return this;
    }

    public UserBuilder setWalletEntries(List<WalletEntry> walletEntries) {
        this.walletEntries = walletEntries;
        return this;
    }

    public UserBuilder setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
        return this;
    }

    public User createUser() {
        return new User(
                userId,
                username,
                password,
                role,
                externalFunds,
                walletEntries,
                transactions
        );
    }
}