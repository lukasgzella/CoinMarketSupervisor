package com.gzella.coinMarketSupervisor.persistence.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "walletEntries")
@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class WalletEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long WalletEntryId;
    @Column
    private String coinSymbol;
    @Column(scale = 10, precision = 20)
    private BigDecimal amount;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    private User user;

    public WalletEntry(String coinSymbol, User user, BigDecimal amount) {
        this.coinSymbol = coinSymbol;
        this.user = user;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "WalletEntry{" +
                "WalletEntryId=" + WalletEntryId +
                ", coinSymbol='" + coinSymbol + '\'' +
                ", amount=" + amount +
                '}';
    }
}