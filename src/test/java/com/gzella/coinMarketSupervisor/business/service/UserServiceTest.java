package com.gzella.coinMarketSupervisor.business.service;

import com.gzella.coinMarketSupervisor.business.exceptions.NoSuchUserException;
import com.gzella.coinMarketSupervisor.business.exceptions.UserAlreadyExistsException;
import com.gzella.coinMarketSupervisor.persistence.entity.User;
import com.gzella.coinMarketSupervisor.persistence.entity.UserBuilder;
import com.gzella.coinMarketSupervisor.persistence.repository.UserRepository;
import com.gzella.coinMarketSupervisor.persistence.repository.WalletEntryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private WalletEntryRepository walletEntryRepository;
    private UserService userService;

    @BeforeEach
    public void mockFields() {
        passwordEncoder = mock(PasswordEncoder.class);
        userRepository = mock(UserRepository.class);
        userService = new UserService(
                passwordEncoder,
                userRepository,
                walletEntryRepository
        );
    }

    private User initUser() {
        return new UserBuilder()
                .setUsername("JohnDoe")
                .setPassword("encodedPassword")
                .setRole("USER")
                .setExternalFunds(BigDecimal.valueOf(10000))
                .createUser();
    }

    @Test
    public void registerNewUser_allParamsOk_userRegistered() {
        //given
        User actual = new UserBuilder()
                .setUsername("JohnDoe")
                .setPassword("password")
                .createUser();
        User expected = initUser();
        when(userRepository.existsByUsername(actual.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(actual.getPassword())).thenReturn("encodedPassword");
        //when
        userService.registerNewUser(actual);
        //then
        assertEquals(expected, actual);
        verify(userRepository).save(expected);
    }

    @Test
    public void registerNewUser_tryingToRegisterExistingUser_throwsUserAlreadyExistsException() {
        //given
        User actual = new UserBuilder()
                .setUsername("JohnDoe")
                .setPassword("password")
                .createUser();
        when(userRepository.existsByUsername(actual.getUsername())).thenReturn(true);
        //when/then
        assertThrows(UserAlreadyExistsException.class, () -> userService.registerNewUser(actual));
    }

    @Test
    public void getUserByUsername_allParamsOk_returnUser() {
        //given
        String actualUserName = "JohnDoe";
        User expected = initUser();
        when(userRepository.findUserByUsername(actualUserName)).thenReturn(Optional.of(expected));
        //when
        expected = userService.getUserByUsername(actualUserName);
        //then
        verify(userRepository).findUserByUsername(actualUserName);
        assertEquals(expected.getUsername(), actualUserName);
    }

    @Test
    public void getUserByUsername_nonExistingUser_throwsNoSuchUserException() {
        //given
        String actualUserName = "JohnDoe";
        when(userRepository.findUserByUsername(actualUserName)).thenReturn(Optional.empty());
        //when/then
        assertThrows(NoSuchUserException.class,
                () -> userService.getUserByUsername(actualUserName),
                "There is no such user in database.");
    }
}