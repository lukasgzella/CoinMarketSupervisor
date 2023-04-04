package com.gzella.coinMarketSupervisor.business.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    private String userCoin;
    private BigDecimal ucPrice;
    private BigDecimal amountUcToExchange;
    private String coinToExchange;
    private BigDecimal ctePrice;
    private BigDecimal amountCteAfterExchange;
    private String timeStamp;
}