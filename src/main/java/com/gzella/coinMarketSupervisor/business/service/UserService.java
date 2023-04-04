package com.gzella.coinMarketSupervisor.business.service;

import com.gzella.coinMarketSupervisor.business.exceptions.NoSuchUserException;
import com.gzella.coinMarketSupervisor.business.exceptions.UserAlreadyExistsException;
import com.gzella.coinMarketSupervisor.persistence.entity.User;
import com.gzella.coinMarketSupervisor.persistence.entity.WalletEntry;
import com.gzella.coinMarketSupervisor.persistence.repository.UserRepository;
import com.gzella.coinMarketSupervisor.persistence.repository.WalletEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final WalletEntryRepository walletEntryRepository;

    public void registerNewUser(User user) throws UserAlreadyExistsException {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistsException("There is such user already");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        user.setExternalFunds(BigDecimal.valueOf(10000));

        userRepository.save(user);
    }

    public User getUserByUsername(String username) {
        return userRepository.findUserByUsername(username).orElseThrow(NoSuchUserException::new);
    }

    //    test method......................
    //    remove this method before deployment, remove field WalletEntryService from UserService
    //    and from UserServiceTest.
    public void addEntry(WalletEntry walletEntry, User user) {
        walletEntry.setUser(user);
        walletEntryRepository.save(walletEntry);
    }
}