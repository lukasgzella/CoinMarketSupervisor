package com.gzella.coinMarketSupervisor.presentation.menu;

import com.gzella.coinMarketSupervisor.business.service.WalletEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;

@Controller
@RequestMapping("/menu/internal")
public class InternalExternalFundsController {

    @Autowired
    WalletEntryService walletEntryService;

    @GetMapping(path = "/deposit", produces = "text/html")
    public String depositUsdt() {
        walletEntryService.deposit(BigDecimal.valueOf(1000));
        return "menu/internal";
    }

    @GetMapping(path = "/withdraw", produces = "text/html")
    public String withdrawUsdt() {
        walletEntryService.withdraw(BigDecimal.valueOf(1000));
        return "menu/internal";
    }
}
