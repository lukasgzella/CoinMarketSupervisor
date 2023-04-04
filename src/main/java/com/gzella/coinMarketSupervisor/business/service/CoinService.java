package com.gzella.coinMarketSupervisor.business.service;

import com.gzella.coinMarketSupervisor.business.dto.CoinDTO;
import com.gzella.coinMarketSupervisor.business.dto.TransactionDTO;
import com.gzella.coinMarketSupervisor.business.dto.TransactionDTOBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CoinService {

    public BigDecimal getActualPrice(String coinSymbol) {
        if (coinSymbol.equals("USDT")) {
            return BigDecimal.ONE;
        }
        WebClient client = WebClient.create();
        String BINANCE_PRICE_URI = "https://api.binance.com/api/v3/ticker/price?symbol=" + coinSymbol + "USDT";
        WebClient.ResponseSpec responseSpec = client.get()
                .uri(BINANCE_PRICE_URI)
                .retrieve();
        CoinDTO coin = responseSpec.bodyToMono(CoinDTO.class).block();
        BigDecimal price = BigDecimal.valueOf(Double.parseDouble(coin.getPrice()));
        return price;
    }

    public TransactionDTO getUpdatedTransactionDTO(TransactionDTO t) {
        BigDecimal ucPrice = getActualPrice(t.getUserCoin());
        BigDecimal ctePrice = getActualPrice(t.getCoinToExchange());
        BigDecimal amountCteAfterExchange = t.getAmountUcToExchange().multiply(ucPrice).divide(ctePrice, RoundingMode.HALF_UP);
        System.out.println(amountCteAfterExchange);
        return new TransactionDTOBuilder()
                .setUserCoin(t.getUserCoin())
                .setUcPrice(ucPrice)
                .setAmountUcToExchange(t.getAmountUcToExchange())
                .setCoinToExchange(t.getCoinToExchange())
                .setCtePrice(ctePrice)
                .setAmountCteAfterExchange(amountCteAfterExchange)
                .createTransactionDTO();
    }
}
