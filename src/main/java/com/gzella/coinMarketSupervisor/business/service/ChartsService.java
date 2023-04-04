package com.gzella.coinMarketSupervisor.business.service;

import com.gzella.coinMarketSupervisor.business.dto.charts.ChartDetailsDTO;
import com.gzella.coinMarketSupervisor.business.dto.charts.Interval;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ChartsService {

    @Autowired
    WebsocketService websocketService;

    public List<String> getAvailableIntervals() {
        return Stream.of(Interval.values())
                .map(interval -> interval.symbol)
                .collect(Collectors.toList());
    }

    public void initDataStream(ChartDetailsDTO selectedChartDetails) throws Exception {
        websocketService.stopBinanceWSStream();
        websocketService.startBinanceWSStream(selectedChartDetails);
        websocketService.startIOServer();
    }
}
