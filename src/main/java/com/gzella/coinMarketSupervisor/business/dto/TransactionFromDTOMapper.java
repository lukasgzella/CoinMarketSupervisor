package com.gzella.coinMarketSupervisor.business.dto;

import com.gzella.coinMarketSupervisor.persistence.entity.Transaction;
import com.gzella.coinMarketSupervisor.persistence.entity.TransactionBuilder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.function.Function;

@Service
public class TransactionFromDTOMapper implements Function<TransactionDTO, Transaction> {
    @Override
    public Transaction apply(TransactionDTO t) {
        return new TransactionBuilder()
                .setUserCoin(t.getUserCoin())
                .setUcPrice(t.getUcPrice())
                .setAmountUcToExchange(t.getAmountUcToExchange())
                .setCoinToExchange(t.getCoinToExchange())
                .setCtePrice(t.getCtePrice())
                .setAmountCteAfterExchange(t.getAmountCteAfterExchange())
                .setTimeStamp(LocalDateTime.now())
                .createTransaction();
    }
}