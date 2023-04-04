package com.gzella.coinMarketSupervisor.business.dto;

import com.gzella.coinMarketSupervisor.persistence.entity.Transaction;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.function.Function;

@Service
public class TransactionToDTOMapper implements Function<Transaction, TransactionDTO> {
    @Override
    public TransactionDTO apply(Transaction t) {
        return new TransactionDTOBuilder()
                .setUserCoin(t.getUserCoin())
                .setUcPrice(t.getUcPrice())
                .setAmountUcToExchange(t.getAmountUcToExchange())
                .setCoinToExchange(t.getCoinToExchange())
                .setCtePrice(t.getCtePrice())
                .setAmountCteAfterExchange(t.getAmountCteAfterExchange())
                .setTimeStamp(t.getTimeStamp()
                        .format(DateTimeFormatter.ofPattern("HH:mm:ss  dd-MM-yyyy")))
                .createTransactionDTO();
    }
}
