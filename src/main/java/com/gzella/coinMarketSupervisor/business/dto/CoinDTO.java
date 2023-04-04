package com.gzella.coinMarketSupervisor.business.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoinDTO {
    public String symbol;
    public String price;
}
