package com.gzella.coinMarketSupervisor.business.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoinDTO {
    private String symbol;
    private String price;
}
