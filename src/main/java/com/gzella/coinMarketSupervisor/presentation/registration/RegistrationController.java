package com.gzella.coinMarketSupervisor.presentation.registration;

import com.gzella.coinMarketSupervisor.persistence.entity.User;
import com.gzella.coinMarketSupervisor.persistence.entity.UserBuilder;
import com.gzella.coinMarketSupervisor.persistence.entity.WalletEntry;
import com.gzella.coinMarketSupervisor.business.exceptions.UserAlreadyExistsException;
import com.gzella.coinMarketSupervisor.business.service.UserService;
import com.gzella.coinMarketSupervisor.persistence.entity.WalletEntryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;

@Controller
public class RegistrationController {

    @Autowired
    UserService userService;

    @GetMapping(path = "/home", produces = "text/html")
    public String displayHome() {
        return "starting/home";
    }

    @GetMapping(path = "/", produces = "text/html")
    public String dispHome() {
        return "starting/home";
    }

    @GetMapping(path = "/login", produces = "text/html")
    public String displayLogin() {
        return "starting/login";
    }

    @GetMapping(path = "/registration", produces = "text/html")
    public String registrationForm() {
        return "starting/registration";
    }

    @GetMapping(path = "/register-test-user", produces = "text/html")
    public String registerTestUser() throws UserAlreadyExistsException {
        User user = new UserBuilder().createUser();
        user.setUsername("JohnDoe");
        user.setPassword("Password");
        user.setExternalFunds(BigDecimal.valueOf(10000));
        userService.registerNewUser(user);
        User testUser = userService.getUserByUsername("JohnDoe");
        WalletEntry walletEntry = new WalletEntryBuilder().createWalletEntry();
        walletEntry.setCoinSymbol("DOT");
        walletEntry.setAmount(BigDecimal.valueOf(250.5));
        userService.addEntry(walletEntry, testUser);

        WalletEntry walletEntry1 = new WalletEntryBuilder().createWalletEntry();
        walletEntry1.setCoinSymbol("ADA");
        walletEntry1.setAmount(BigDecimal.valueOf(4000));
        userService.addEntry(walletEntry1, testUser);

        WalletEntry walletEntry2 = new WalletEntryBuilder().createWalletEntry();
        walletEntry2.setCoinSymbol("FIL");
        walletEntry2.setAmount(BigDecimal.valueOf(263.58));
        userService.addEntry(walletEntry2, testUser);

        return "starting/login_test";
    }

    @PostMapping("/register-user")
    public ResponseEntity<User> registerUser(Model model, @RequestBody User user) throws UserAlreadyExistsException {
        userService.registerNewUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
