//package com.gzella.coinMarketSupervisor.presentation;
//
//import com.gzella.coinMarketSupervisor.business.dto.TransactionDTO;
//import com.gzella.coinMarketSupervisor.business.utils.alerts.DiscordUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//// DISCORD ALERT TO DO
//
//@Controller
//@RequestMapping("/menu/exchange")
//public class EventController {
//    @Autowired
//    DiscordUtils discordUtils;
//    @PostMapping("/send-info-to-bot")
//    public ResponseEntity<TransactionDTO> mapTransactionDetails(@RequestBody TransactionDTO transactionDTO) throws InterruptedException {
//        System.out.println("EventController just received info for bot");
//        discordUtils.makeNotification();
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//}