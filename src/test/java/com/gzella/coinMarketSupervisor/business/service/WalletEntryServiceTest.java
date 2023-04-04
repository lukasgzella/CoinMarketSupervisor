package com.gzella.coinMarketSupervisor.business.service;

import com.gzella.coinMarketSupervisor.business.dto.TransactionDTO;
import com.gzella.coinMarketSupervisor.business.dto.TransactionDTOBuilder;
import com.gzella.coinMarketSupervisor.business.dto.WalletEntryDTO;
import com.gzella.coinMarketSupervisor.business.dto.WalletEntryToDTOMapper;
import com.gzella.coinMarketSupervisor.business.exceptions.AmountGreaterThanAvailableException;
import com.gzella.coinMarketSupervisor.persistence.entity.*;
import com.gzella.coinMarketSupervisor.persistence.repository.UserRepository;
import com.gzella.coinMarketSupervisor.persistence.repository.WalletEntryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WalletEntryServiceTest {

    private UserRepository userRepository;
    private WalletEntryRepository walletEntryRepository;
    private SecurityService securityService;
    private TransactionService transactionService;
    private WalletEntryToDTOMapper walletEntryToDTOMapper;
    private WalletEntryService walletEntryService;

    @BeforeEach
    public void mockFields() {
        userRepository = mock(UserRepository.class);
        walletEntryRepository = mock(WalletEntryRepository.class);
        securityService = mock(SecurityService.class);
        transactionService = mock(TransactionService.class);
        walletEntryToDTOMapper = new WalletEntryToDTOMapper(new CoinService() {
            @Override
            public BigDecimal getActualPrice(String coinSymbol) {
                return BigDecimal.ONE;
            }
        });
        walletEntryService = new WalletEntryService(
                userRepository,
                walletEntryRepository,
                securityService,
                transactionService,
                walletEntryToDTOMapper
        );
    }

    private User initUser() {
        return new UserBuilder()
                .setUserId(10L)
                .setUsername("JohnDoe")
                .setPassword("password")
                .setRole("USER")
                .setExternalFunds(BigDecimal.valueOf(10000))
                .setWalletEntries(Collections.emptyList())
                .createUser();
    }

    private List<WalletEntry> initEntries(User user) {
        return List.of(
                new WalletEntryBuilder()
                        .setWalletEntryId(10L)
                        .setCoinSymbol(Coin.ADA.symbol)
                        .setAmount(BigDecimal.TEN)
                        .setUser(user)
                        .createWalletEntry(),
                new WalletEntryBuilder()
                        .setWalletEntryId(11L)
                        .setCoinSymbol(Coin.ETH.symbol)
                        .setAmount(BigDecimal.ONE)
                        .setUser(user)
                        .createWalletEntry()
        );
    }

    private List<WalletEntryDTO> initEntriesDTO() {
        return List.of(
                new WalletEntryDTO(Coin.ADA, BigDecimal.valueOf(4300), BigDecimal.ONE),
                new WalletEntryDTO(Coin.ETH, BigDecimal.valueOf(1.34), BigDecimal.ONE),
                new WalletEntryDTO(Coin.BTC, BigDecimal.valueOf(0.443), BigDecimal.ONE)
        );
    }

    @Test
    public void getInternalAccount_userWithoutWalletEntries_returnedInternalAccount() {
        //given
        User authenticatedUser = initUser();
        when(securityService.getAuthenticatedUser()).thenReturn(authenticatedUser);

        List<WalletEntryDTO> expected = Collections.emptyList();
        //when
        List<WalletEntryDTO> actual = walletEntryService.getInternalAccount();
        //then
        assertEquals(expected, actual);
    }

    @Test
    public void getInternalAccount_userWithWalletEntries_returnedInternalAccount() {
        //given
        User authenticatedUser = initUser();
        List<WalletEntry> entries = initEntries(authenticatedUser);
        authenticatedUser.setWalletEntries(entries);
        when(securityService.getAuthenticatedUser()).thenReturn(authenticatedUser);

        List<WalletEntryDTO> expected = entries
                .stream()
                .map(w -> walletEntryToDTOMapper.apply(w))
                .collect(Collectors.toList());
        //when
        List<WalletEntryDTO> actual = walletEntryService.getInternalAccount();
        //then
        assertEquals(expected, actual);
    }

    @Test
    public void getExternalAccount_allParamsOk_returnedExternalAccount() {
        //given
        User authenticatedUser = initUser();
        when(securityService.getAuthenticatedUser()).thenReturn(authenticatedUser);

        BigDecimal expectedExternalFunds = BigDecimal.valueOf(10000);
        //when
        BigDecimal actualExternalFunds = walletEntryService.getExternalAccount();
        //then
        assertEquals(expectedExternalFunds, actualExternalFunds);
    }

    @Test
    public void getAvailableCoins_returnedAvailableCoins() {
        //given
        List<WalletEntryDTO> expectedAvailableCoins = Stream.of(Coin.values())
                .map(coin -> walletEntryToDTOMapper.apply(coin))
                .collect(Collectors.toList());
        //when
        List<WalletEntryDTO> actualAvailableCoins = walletEntryService.getAvailableCoins();
        //then
        assertEquals(expectedAvailableCoins, actualAvailableCoins);
    }

    @Test
    public void getSelectedAvailableCoin_selectedBTC_returnedBTCWalletEntryDTO() {
        //given
        String coinSymbol = Coin.BTC.symbol;
        WalletEntryDTO expected = new WalletEntryDTO(
                Coin.BTC,
                BigDecimal.ONE);
        //when
        WalletEntryDTO actualSelectedCoin = walletEntryService.getSelectedAvailableCoin(coinSymbol);
        //then
        assertEquals(expected, actualSelectedCoin);
    }

    @Test
    public void getSelectedWalletEntry_selectedADA_returnedADAWalletEntryDTO() {
        //given
        User authenticatedUser = initUser();
        List<WalletEntry> entries = initEntries(authenticatedUser);
        authenticatedUser.setWalletEntries(entries);
        when(securityService.getAuthenticatedUser()).thenReturn(authenticatedUser);

        String coinSymbol = Coin.ADA.symbol;
        WalletEntryDTO expected = new WalletEntryDTO(
                Coin.ADA,
                BigDecimal.valueOf(10).setScale(5, RoundingMode.UNNECESSARY),
                BigDecimal.ONE
        );
        //when
        WalletEntryDTO actualSelectedCoin = walletEntryService.getSelectedWalletEntry(coinSymbol);
        //then
        assertEquals(expected, actualSelectedCoin);
    }

    @Test
    public void searchByCoinSymbol_symbolETH_returnedETHWalletEntryDTO() {
        //given
        List<WalletEntryDTO> entries = initEntriesDTO();
        String coinSymbol = Coin.ETH.symbol;
        WalletEntryDTO expected = new WalletEntryDTO(
                Coin.ETH,
                BigDecimal.valueOf(1.34),
                BigDecimal.ONE
        );
        //when
        WalletEntryDTO actualSearched = walletEntryService.searchByCoinSymbol(entries, coinSymbol);
        //then
        assertEquals(expected, actualSearched);
    }

    @Test
    public void searchByCoinSymbol_symbolLTC_thrownNoSuchElementException() {
        //given
        List<WalletEntryDTO> entries = initEntriesDTO();
        String coinSymbol = Coin.LTC.symbol;
        //when/then
        assertThrows(
                NoSuchElementException.class,
                () -> walletEntryService.searchByCoinSymbol(entries, coinSymbol));
    }

    @Test
    public void deposit_userWithoutUsdtWalletEntry_fundsAddedToInternalAccount() {
        //given
        User authenticatedUser = initUser();
        when(securityService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        BigDecimal amount = BigDecimal.TEN;
        BigDecimal expectedExternalFunds = BigDecimal.valueOf(9990);
        //when
        walletEntryService.deposit(amount);
        //then
        verify(userRepository).save(authenticatedUser);
        verify(walletEntryRepository).save(new WalletEntryBuilder()
                .setCoinSymbol(Coin.USDT.symbol)
                .setUser(authenticatedUser)
                .setAmount(amount)
                .createWalletEntry());
        assertEquals(expectedExternalFunds, authenticatedUser.getExternalFunds());
    }

    @Test
    public void deposit_userWithUsdtWalletEntry_fundsAddedToInternalAccount() {
        //given
        User authenticatedUser = initUser();
        WalletEntry usdtWalletEntry = new WalletEntryBuilder()
                .setCoinSymbol(Coin.USDT.symbol)
                .setUser(authenticatedUser)
                .setAmount(BigDecimal.valueOf(7000))
                .createWalletEntry();
        authenticatedUser.setWalletEntries(List.of(usdtWalletEntry));
        when(securityService.getAuthenticatedUser()).thenReturn(authenticatedUser);

        BigDecimal expectedExternalFunds = BigDecimal.valueOf(9990);
        BigDecimal amount = BigDecimal.TEN;
        //when
        walletEntryService.deposit(amount);
        //then
        verify(userRepository).save(authenticatedUser);
        verify(walletEntryRepository).save(new WalletEntryBuilder()
                .setCoinSymbol(Coin.USDT.symbol)
                .setUser(authenticatedUser)
                .setAmount(BigDecimal.valueOf(7010))
                .createWalletEntry());
        assertEquals(expectedExternalFunds, authenticatedUser.getExternalFunds());
    }

    @Test
    public void deposit_userWithoutExternalFunds_thrownAmountGreaterThanAvailableException() {
        //given
        User authenticatedUser = initUser();
        authenticatedUser.setExternalFunds(BigDecimal.ZERO);
        when(securityService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        BigDecimal amount = BigDecimal.TEN;
        // when/then
        assertThrows(AmountGreaterThanAvailableException.class, () -> walletEntryService.deposit(amount));
    }

    @Test
    public void withdraw_allParamsOk_fundsAddedToExternalAccount() {
        //given
        User authenticatedUser = initUser();
        WalletEntry usdtWalletEntry = new WalletEntryBuilder()
                .setCoinSymbol(Coin.USDT.symbol)
                .setUser(authenticatedUser)
                .setAmount(BigDecimal.valueOf(7000))
                .createWalletEntry();
        authenticatedUser.setWalletEntries(List.of(usdtWalletEntry));
        when(securityService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        BigDecimal expectedExternalFunds = BigDecimal.valueOf(10010);
        BigDecimal amount = BigDecimal.TEN;
        //when
        walletEntryService.withdraw(amount);
        //then
        verify(userRepository).save(authenticatedUser);
        verify(walletEntryRepository).save(new WalletEntryBuilder()
                .setCoinSymbol(Coin.USDT.symbol)
                .setUser(authenticatedUser)
                .setAmount(BigDecimal.valueOf(6990))
                .createWalletEntry());
        assertEquals(expectedExternalFunds, authenticatedUser.getExternalFunds());
    }

    @Test
    public void withdraw_userWithoutUsdtEntry_thrownAmountGreaterThanAvailableException() {
        //given
        User authenticatedUser = initUser();
        authenticatedUser.setExternalFunds(BigDecimal.ZERO);
        when(securityService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        BigDecimal amount = BigDecimal.TEN;
        // when/then
        assertThrows(AmountGreaterThanAvailableException.class, () -> walletEntryService.withdraw(amount));
    }

    @Test
    public void proceedTransaction_allParamsOk_TransactionRegistered() {
        //given
        TransactionDTO transactionDTO = new TransactionDTOBuilder()
                .setUserCoin(Coin.ETH.symbol)
                .setUcPrice(BigDecimal.valueOf(1000))
                .setAmountUcToExchange(BigDecimal.ONE)
                .setCoinToExchange(Coin.LTC.symbol)
                .setCtePrice(BigDecimal.valueOf(100))
                .setAmountCteAfterExchange(BigDecimal.TEN)
                .createTransactionDTO();
        User authenticatedUser = initUser();
        List<WalletEntry> entries = initEntries(authenticatedUser);
        authenticatedUser.setWalletEntries(entries);

        when(securityService.getAuthenticatedUser()).thenReturn(authenticatedUser);

        WalletEntry expectedCteEntry = new WalletEntryBuilder()
                .setWalletEntryId(10L)
                .setCoinSymbol(Coin.LTC.symbol)
                .setAmount(BigDecimal.TEN)
                .setUser(authenticatedUser)
                .createWalletEntry();
        WalletEntry expectedUcEntry = new WalletEntryBuilder()
                .setWalletEntryId(11L)
                .setCoinSymbol(Coin.ETH.symbol)
                .setAmount(BigDecimal.ZERO)
                .setUser(authenticatedUser)
                .createWalletEntry();
        //when
        walletEntryService.proceedTransaction(transactionDTO);
        //then
        verify(walletEntryRepository).save(expectedCteEntry);
        verify(walletEntryRepository).save(expectedUcEntry);
        verify(transactionService).registerNewTransaction(transactionDTO);
    }

    @Test
    public void getUserWalletEntry_allParamsOk_returnedWalletEntry() {
        //given
        User authenticatedUser = initUser();
        List<WalletEntry> entries = initEntries(authenticatedUser);
        authenticatedUser.setWalletEntries(entries);
        when(securityService.getAuthenticatedUser()).thenReturn(authenticatedUser);

        String coinSymbol = Coin.ETH.symbol;
        WalletEntry expected = new WalletEntryBuilder()
                .setCoinSymbol(Coin.ETH.symbol)
                .setUser(authenticatedUser)
                .setAmount(BigDecimal.ONE)
                .createWalletEntry();
        //when
        WalletEntry actual = walletEntryService.getUserWalletEntry(coinSymbol);
        //then
        assertEquals(expected, actual);
    }

    @Test
    public void getUserWalletEntry_userWithoutWalletEntries_returnedWalletEntryWithZeroAmount() {
        //given
        User authenticatedUser = initUser();
        when(securityService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        String coinSymbol = Coin.ETH.symbol;
        WalletEntry expected = new WalletEntryBuilder()
                .setCoinSymbol(Coin.ETH.symbol)
                .setUser(authenticatedUser)
                .setAmount(BigDecimal.ZERO)
                .createWalletEntry();
        //when
        WalletEntry actual = walletEntryService.getUserWalletEntry(coinSymbol);
        //then
        assertEquals(expected, actual);
    }

    @Test
    public void getUserWalletEntry_userWithoutInvokedEntry_returnedWalletEntryWithZeroAmount() {
        //given
        User authenticatedUser = initUser();
        List<WalletEntry> entries = initEntries(authenticatedUser);
        authenticatedUser.setWalletEntries(entries);
        when(securityService.getAuthenticatedUser()).thenReturn(authenticatedUser);

        String coinSymbol = Coin.FIL.symbol;
        WalletEntry expected = new WalletEntryBuilder()
                .setCoinSymbol(Coin.FIL.symbol)
                .setUser(authenticatedUser)
                .setAmount(BigDecimal.ZERO)
                .createWalletEntry();
        //when
        WalletEntry actual = walletEntryService.getUserWalletEntry(coinSymbol);
        //then
        assertEquals(expected, actual);
    }

    @Test
    public void validateAmount_amountSmallerThanAmountFrom_returnedTrue() {
        //given
        BigDecimal amount = BigDecimal.valueOf(5);
        BigDecimal amountFrom = BigDecimal.valueOf(6);
        // when/then
        assertTrue(walletEntryService.validateAmount(amount, amountFrom));
    }

    @Test
    public void validateAmount_amountEqualToAmountFrom_returnedTrue() {
        //given
        BigDecimal amount = BigDecimal.valueOf(5);
        BigDecimal amountFrom = BigDecimal.valueOf(5);
        // when/then
        assertTrue(walletEntryService.validateAmount(amount, amountFrom));
    }

    @Test
    public void validateAmount_amountGreaterThanAmountFrom_returnedFalse() {
        //given
        BigDecimal amount = BigDecimal.valueOf(5);
        BigDecimal amountFrom = BigDecimal.valueOf(4);
        // when/then
        assertFalse(walletEntryService.validateAmount(amount, amountFrom));
    }

    @Test
    public void calculateTotalBalance_allParamsOk_returnedBalance() {
        //given
        List<WalletEntryDTO> entries = List.of(
                new WalletEntryDTO(Coin.BNB, BigDecimal.TEN, BigDecimal.valueOf(300)),
                new WalletEntryDTO(Coin.FIL, BigDecimal.valueOf(200), BigDecimal.valueOf(10)),
                new WalletEntryDTO(Coin.BTC, BigDecimal.ONE, BigDecimal.valueOf(25000)),
                new WalletEntryDTO(Coin.ETH, BigDecimal.ONE, BigDecimal.valueOf(2000))
        );
        BigDecimal expected = BigDecimal.valueOf(32000).setScale(3, RoundingMode.UP);
        //when
        BigDecimal actual = walletEntryService.calculateTotalBalance(entries);
        //then
        assertEquals(expected, actual);
    }

    @Test
    public void calculateTotalBalance_entriesWithZeroAmount_returnedBalance() {
        //given
        List<WalletEntryDTO> entries = List.of(
                new WalletEntryDTO(Coin.BNB, BigDecimal.ZERO, BigDecimal.valueOf(300)),
                new WalletEntryDTO(Coin.FIL, BigDecimal.ZERO, BigDecimal.valueOf(10)),
                new WalletEntryDTO(Coin.BTC, BigDecimal.ONE, BigDecimal.valueOf(25000)),
                new WalletEntryDTO(Coin.ETH, BigDecimal.ONE, BigDecimal.valueOf(2000))
        );
        BigDecimal expected = BigDecimal.valueOf(27000).setScale(3, RoundingMode.UP);
        //when
        BigDecimal actual = walletEntryService.calculateTotalBalance(entries);
        //then
        assertEquals(expected, actual);
    }
}