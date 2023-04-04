package com.gzella.coinMarketSupervisor.persistence.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long transactionId;
    @Column
    private String userCoin;
    @Column
    private BigDecimal ucPrice;
    @Column(scale = 10, precision = 20)
    private BigDecimal amountUcToExchange;
    @Column
    private String coinToExchange;
    @Column
    private BigDecimal ctePrice;
    @Column(scale = 10, precision = 20)
    private BigDecimal amountCteAfterExchange;
    @Column
    private LocalDateTime timeStamp;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    private User user;
}
