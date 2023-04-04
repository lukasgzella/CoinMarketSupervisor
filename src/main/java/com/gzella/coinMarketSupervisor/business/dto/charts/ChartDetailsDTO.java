package com.gzella.coinMarketSupervisor.business.dto.charts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Getter
@Setter
public class ChartDetailsDTO {

    private String coinSymbol;
    private String interval;

}
