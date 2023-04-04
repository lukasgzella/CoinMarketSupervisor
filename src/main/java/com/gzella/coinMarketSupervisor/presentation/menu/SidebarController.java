package com.gzella.coinMarketSupervisor.presentation.menu;

import com.gzella.coinMarketSupervisor.business.dto.TransactionDTO;
import com.gzella.coinMarketSupervisor.business.dto.WalletEntryDTO;
import com.gzella.coinMarketSupervisor.business.service.ChartsService;
import com.gzella.coinMarketSupervisor.business.service.TransactionService;
import com.gzella.coinMarketSupervisor.business.service.WalletEntryService;
import com.gzella.coinMarketSupervisor.business.dto.charts.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/menu")
public class SidebarController {

    @Autowired
    WalletEntryService walletEntryService;
    @Autowired
    TransactionService transactionService;
    @Autowired
    ChartsService chartsService;

    @GetMapping(path = "/internal", produces = "text/html")
    public String displayInternalHTML(Model model) {
        List<WalletEntryDTO> entries = walletEntryService.getInternalAccount();
        BigDecimal totalBalance = walletEntryService.calculateTotalBalance(entries);
        model.addAttribute("entries", entries);
        model.addAttribute("balance", totalBalance);
        return "menu/internal";
    }

    @GetMapping(path = "/external", produces = "text/html")
    public String displayExternalHTML(Model model) {
        BigDecimal funds = walletEntryService.getExternalAccount();
        model.addAttribute("funds", funds);
        return "menu/external";
    }

    @GetMapping(path = "/available", produces = "text/html")
    public String displayAvailableCoinsHTML(Model model) {
        List<WalletEntryDTO> entries = walletEntryService.getAvailableCoins();
        model.addAttribute("entries", entries);
        return "menu/available";
    }

    @GetMapping(path = "/exchange", produces = "text/html")
    public String displayExchangeHTML(Model model) {
        List<WalletEntryDTO> ins = walletEntryService.getInternalAccount();
        List<WalletEntryDTO> entries = walletEntryService.getAvailableCoins();
        model.addAttribute("ins", ins);
        model.addAttribute("entries", entries);
        return "menu/exchange/exchange";
    }

    @GetMapping(path = "/transactions", produces = "text/html")
    public String displayTransactionsHTML(Model model) {
        List<TransactionDTO> entries = transactionService.getTransactions();
        model.addAttribute("entries", entries);
        return "menu/transactions";
    }

    @GetMapping(path = "/charts", produces = "text/html")
    public String displayChartsHTML(Model model) {
        List<WalletEntryDTO> entries = walletEntryService.getAvailableCoins();
        List<String> intervals = chartsService.getAvailableIntervals();
        model.addAttribute("entries", entries);
        model.addAttribute("intervals", intervals);
        return "menu/charts/charts";
    }


    @GetMapping(path = "/alerts", produces = "text/html")
    public String displayAlertsHTML() {
        return "menu/alerts";
    }

    @GetMapping(path = "/info", produces = "text/html")
    public String displayHelpHTML() {
        return "menu/info";
    }

}