package com.gzella.coinMarketSupervisor.business.dto;

import com.gzella.coinMarketSupervisor.business.service.CoinService;
import com.gzella.coinMarketSupervisor.persistence.entity.Coin;
import com.gzella.coinMarketSupervisor.persistence.entity.WalletEntry;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class WalletEntryToDTOMapper implements Function<WalletEntry, WalletEntryDTO> {

    private final CoinService coinService;

    @Override
    public WalletEntryDTO apply(WalletEntry w) {
        return new WalletEntryDTO(
                Coin.valueOf(w.getCoinSymbol()),
                w.getAmount().setScale(5, RoundingMode.UNNECESSARY),
                coinService.getActualPrice(w.getCoinSymbol())
        );
    }

    public WalletEntryDTO apply(Coin c) {
        return new WalletEntryDTO(
                c,
                coinService.getActualPrice(c.symbol)
        );
    }
}