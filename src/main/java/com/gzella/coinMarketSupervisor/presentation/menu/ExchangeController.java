package com.gzella.coinMarketSupervisor.presentation.menu;

import com.gzella.coinMarketSupervisor.business.dto.TransactionDTO;
import com.gzella.coinMarketSupervisor.business.dto.WalletEntryDTO;
import com.gzella.coinMarketSupervisor.business.exceptions.IncorrectTransactionDetailsException;
import com.gzella.coinMarketSupervisor.business.service.CoinService;
import com.gzella.coinMarketSupervisor.business.service.WalletEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/menu/exchange")
public class ExchangeController {

    TransactionDTO currentTransaction;

    @Autowired
    CoinService coinService;
    @Autowired
    WalletEntryService walletEntryService;

    @PostMapping("/set-pair")
    public ResponseEntity<TransactionDTO> mapTransactionPair(@RequestBody TransactionDTO transactionDTO) {
        currentTransaction = transactionDTO;
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/set-amount")
    public String setAmount(Model model) {
        WalletEntryDTO selectedUserCoin = walletEntryService.getSelectedWalletEntry(currentTransaction.getUserCoin());
        WalletEntryDTO selectedCoinToExchange = walletEntryService.getSelectedAvailableCoin(currentTransaction.getCoinToExchange());
        model.addAttribute("userCoin", selectedUserCoin);
        model.addAttribute("coinToExchange", selectedCoinToExchange);
        model.addAttribute("currentTransaction", currentTransaction);
        return "menu/exchange/set_amount";
    }

    @PostMapping("/set-details")
    public ResponseEntity<TransactionDTO> mapTransactionDetails(@RequestBody TransactionDTO t) {
        currentTransaction = coinService.getUpdatedTransactionDTO(t);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping("/get-details")
    public ResponseEntity<TransactionDTO> getTransactionDetails() {
        return new ResponseEntity<TransactionDTO>(currentTransaction, HttpStatus.OK);
    }

    @PostMapping("/confirm")
    public ResponseEntity<TransactionDTO> confirmTransaction(@RequestBody TransactionDTO t) {
        if (t.equals(currentTransaction)) {
            walletEntryService.proceedTransaction(t);
        } else throw new IncorrectTransactionDetailsException();
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}