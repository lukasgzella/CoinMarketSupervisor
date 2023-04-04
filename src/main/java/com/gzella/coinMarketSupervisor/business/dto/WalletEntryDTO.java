package com.gzella.coinMarketSupervisor.business.dto;

import com.gzella.coinMarketSupervisor.persistence.entity.Coin;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.RoundingMode;

@EqualsAndHashCode
@ToString
public class WalletEntryDTO {
    public Coin coin;
    public BigDecimal amount;
    public BigDecimal price;
    public BigDecimal value;

    public WalletEntryDTO(Coin coin, BigDecimal amount, BigDecimal price) {
        this.coin = coin;
        this.amount = amount;
        this.price = price;
        this.value = (amount.multiply(price)).setScale(3, RoundingMode.UP);
    }

    public WalletEntryDTO(Coin coin, BigDecimal price) {
        this.coin = coin;
        this.price = price;
    }
}