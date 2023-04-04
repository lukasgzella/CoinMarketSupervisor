package com.gzella.coinMarketSupervisor.business.service;

import com.gzella.coinMarketSupervisor.business.dto.TransactionDTO;
import com.gzella.coinMarketSupervisor.business.dto.WalletEntryDTO;
import com.gzella.coinMarketSupervisor.business.dto.WalletEntryToDTOMapper;
import com.gzella.coinMarketSupervisor.business.exceptions.AmountGreaterThanAvailableException;
import com.gzella.coinMarketSupervisor.persistence.entity.Coin;
import com.gzella.coinMarketSupervisor.persistence.entity.User;
import com.gzella.coinMarketSupervisor.persistence.entity.WalletEntry;
import com.gzella.coinMarketSupervisor.persistence.entity.WalletEntryBuilder;
import com.gzella.coinMarketSupervisor.persistence.repository.UserRepository;
import com.gzella.coinMarketSupervisor.persistence.repository.WalletEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class WalletEntryService {

    private final UserRepository userRepository;
    private final WalletEntryRepository walletEntryRepository;
    private final SecurityService securityService;
    private final TransactionService transactionService;
    private final WalletEntryToDTOMapper walletEntryToDTOMapper;

    public List<WalletEntryDTO> getInternalAccount() {
        return securityService.getAuthenticatedUser().getWalletEntries()
                .stream()
                .map(w -> walletEntryToDTOMapper.apply(w))
                .collect(Collectors.toList());
    }

    public BigDecimal getExternalAccount() {
        return securityService.getAuthenticatedUser().getExternalFunds();
    }

    public List<WalletEntryDTO> getAvailableCoins() {
        return Stream.of(Coin.values())
                .map(coin -> walletEntryToDTOMapper.apply(coin))
                .collect(Collectors.toList());
    }

    public WalletEntryDTO getSelectedAvailableCoin(String coinSymbol) {
        return searchByCoinSymbol(getAvailableCoins(), coinSymbol);
    }

    public WalletEntryDTO getSelectedWalletEntry(String coinSymbol) {
        return searchByCoinSymbol(getInternalAccount(), coinSymbol);
    }

    public WalletEntryDTO searchByCoinSymbol(List<WalletEntryDTO> entries, String coinSymbol) {
        return entries
                .stream()
                .filter(e -> e.coin.symbol.equals(coinSymbol))
                .findFirst()
                .orElseThrow();
    }

    @Transactional
    public void deposit(BigDecimal amount) {
        User user = securityService.getAuthenticatedUser();
        BigDecimal externalFunds = user.getExternalFunds();
        WalletEntry usdtEntry = getUserWalletEntry(Coin.USDT.symbol);

        if (validateAmount(amount, externalFunds)) {
            user.setExternalFunds(externalFunds.subtract(amount));
            usdtEntry.setAmount(usdtEntry.getAmount().add(amount));

            userRepository.save(user);
            walletEntryRepository.save(usdtEntry);
        } else {
            throw new AmountGreaterThanAvailableException("Amount to deposit exceeds external funds.");
        }
    }

    @Transactional
    public void withdraw(BigDecimal amount) {
        User user = securityService.getAuthenticatedUser();
        BigDecimal externalFunds = user.getExternalFunds();
        WalletEntry usdtEntry = getUserWalletEntry(Coin.USDT.symbol);

        if (validateAmount(amount, usdtEntry.getAmount())) {
            user.setExternalFunds(externalFunds.add(amount));
            usdtEntry.setAmount(usdtEntry.getAmount().subtract(amount));

            userRepository.save(user);
            walletEntryRepository.save(usdtEntry);
        } else {
            throw new AmountGreaterThanAvailableException("Amount to withdraw exceeds available funds.");
        }
    }

    @Transactional
    public void proceedTransaction(TransactionDTO transactionDTO) {
        WalletEntry cteEntry = getUserWalletEntry(transactionDTO.getCoinToExchange());
        WalletEntry ucEntry = getUserWalletEntry(transactionDTO.getUserCoin());

        cteEntry.setAmount(cteEntry.getAmount().add(transactionDTO.getAmountCteAfterExchange()));
        if (validateAmount(transactionDTO.getAmountUcToExchange(), ucEntry.getAmount())) {
            ucEntry.setAmount(ucEntry.getAmount().subtract(transactionDTO.getAmountUcToExchange()));
        }

        walletEntryRepository.save(cteEntry);
        walletEntryRepository.save(ucEntry);
        transactionService.registerNewTransaction(transactionDTO);
    }

    public WalletEntry getUserWalletEntry(String coinSymbol) {
        User user = securityService.getAuthenticatedUser();
        List<WalletEntry> entries = user.getWalletEntries();
        if (entries == null) {
            return new WalletEntryBuilder()
                    .setCoinSymbol(coinSymbol)
                    .setUser(user)
                    .setAmount(BigDecimal.ZERO)
                    .createWalletEntry();
        }
        return entries
                .stream()
                .filter(walletEntry -> walletEntry.getCoinSymbol().equals(coinSymbol))
                .findAny().orElse(new WalletEntryBuilder()
                        .setCoinSymbol(coinSymbol)
                        .setUser(user)
                        .setAmount(BigDecimal.ZERO)
                        .createWalletEntry());
    }

    public boolean validateAmount(BigDecimal amount, BigDecimal amountFrom) {
        return amount.compareTo(amountFrom) <= 0;
    }

    public BigDecimal calculateTotalBalance(List<WalletEntryDTO> entries) {
        return entries
                .stream()
                .map(walletEntryDTO -> walletEntryDTO.value)
                .reduce(BigDecimal::add)
                .orElseGet(() -> BigDecimal.ZERO);
    }
}