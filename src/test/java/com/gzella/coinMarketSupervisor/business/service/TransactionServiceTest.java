package com.gzella.coinMarketSupervisor.business.service;

import com.gzella.coinMarketSupervisor.business.dto.TransactionDTO;
import com.gzella.coinMarketSupervisor.business.dto.TransactionDTOBuilder;
import com.gzella.coinMarketSupervisor.business.dto.TransactionFromDTOMapper;
import com.gzella.coinMarketSupervisor.business.dto.TransactionToDTOMapper;
import com.gzella.coinMarketSupervisor.persistence.entity.*;
import com.gzella.coinMarketSupervisor.persistence.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    private SecurityService securityService;
    private TransactionRepository transactionRepository;
    private TransactionFromDTOMapper transactionFromDTOMapper;
    private TransactionToDTOMapper transactionToDTOMapper;
    private TransactionService transactionService;

    @BeforeEach
    public void mockFields() {
        securityService = mock(SecurityService.class);
        transactionRepository = mock(TransactionRepository.class);
        transactionFromDTOMapper = mock(TransactionFromDTOMapper.class);
        transactionToDTOMapper = new TransactionToDTOMapper() {
            @Override
            public TransactionDTO apply(Transaction t) {
                return new TransactionDTOBuilder()
                        .setUserCoin(t.getUserCoin())
                        .createTransactionDTO();
            }
        };
        transactionService = new TransactionService(
                securityService,
                transactionRepository,
                transactionFromDTOMapper,
                transactionToDTOMapper
        );
    }

    private User initUser() {
        return new UserBuilder()
                .setUserId(10L)
                .setUsername("JohnDoe")
                .setPassword("encodedPassword")
                .setRole("USER")
                .setExternalFunds(BigDecimal.valueOf(10000))
                .createUser();
    }

    private Set<Transaction> initTransactions() {
        return Set.of(
                new TransactionBuilder()
                        .setUserCoin(Coin.LTC.symbol)
                        .setUcPrice(BigDecimal.valueOf(100))
                        .setAmountUcToExchange(BigDecimal.ONE)
                        .setCoinToExchange(Coin.ETH.symbol)
                        .setCtePrice(BigDecimal.valueOf(1000))
                        .setAmountCteAfterExchange(BigDecimal.TEN)
                        .setTimeStamp(LocalDateTime.MIN)
                        .createTransaction(),
                new TransactionBuilder()
                        .setUserCoin(Coin.ETH.symbol)
                        .setUcPrice(BigDecimal.valueOf(1000))
                        .setAmountUcToExchange(BigDecimal.ONE)
                        .setCoinToExchange(Coin.LTC.symbol)
                        .setCtePrice(BigDecimal.valueOf(100))
                        .setAmountCteAfterExchange(BigDecimal.TEN)
                        .setTimeStamp(LocalDateTime.MIN)
                        .createTransaction());
    }

    @Test
    public void registerNewTransaction_allParamsOk_transactionRegistered() {
        //given
        User user = initUser();
        when(securityService.getAuthenticatedUser()).thenReturn(user);
        TransactionDTO transactionDTO = new TransactionDTOBuilder()
                .setUserCoin(Coin.ETH.symbol)
                .setUcPrice(BigDecimal.valueOf(1000))
                .setAmountUcToExchange(BigDecimal.ONE)
                .setCoinToExchange(Coin.LTC.symbol)
                .setCtePrice(BigDecimal.valueOf(100))
                .setAmountCteAfterExchange(BigDecimal.TEN)
                .createTransactionDTO();
        Transaction transaction = new TransactionBuilder()
                .setUserCoin(Coin.ETH.symbol)
                .setUcPrice(BigDecimal.valueOf(1000))
                .setAmountUcToExchange(BigDecimal.ONE)
                .setCoinToExchange(Coin.LTC.symbol)
                .setCtePrice(BigDecimal.valueOf(100))
                .setAmountCteAfterExchange(BigDecimal.TEN)
                .setTimeStamp(LocalDateTime.MIN)
                .createTransaction();
        when(transactionFromDTOMapper.apply(transactionDTO)).thenReturn(transaction);
        //when
        transactionService.registerNewTransaction(transactionDTO);
        //then
        verify(transactionRepository).save(transaction);
        assertEquals(user, transaction.getUser());
    }

    @Test
    public void getTransactions_allParamsOk_returnTransactionDTOList() {
        //given
        User user = initUser();
        when(securityService.getAuthenticatedUser()).thenReturn(user);
        List<TransactionDTO> expected = List.of(
                new TransactionDTOBuilder()
                        .setUserCoin(Coin.ETH.symbol)
                        .createTransactionDTO(),
                new TransactionDTOBuilder()
                        .setUserCoin(Coin.LTC.symbol)
                        .createTransactionDTO()
        );
        when(transactionRepository.findAllByUser(user)).thenReturn(initTransactions());
        //when
        List<TransactionDTO> actual = transactionService.getTransactions();
        //then
        verify(transactionRepository).findAllByUser(user);
        assertEquals(expected, actual);
    }
}