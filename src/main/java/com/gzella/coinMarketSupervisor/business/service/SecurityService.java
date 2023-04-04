package com.gzella.coinMarketSupervisor.business.service;

import com.gzella.coinMarketSupervisor.business.exceptions.NoSuchUserException;
import com.gzella.coinMarketSupervisor.persistence.entity.User;
import com.gzella.coinMarketSupervisor.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityService {

    @Autowired
    UserRepository userRepository;

    public String currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    public User getAuthenticatedUser() {
        return userRepository.findUserByUsername(currentUser()).orElseThrow(NoSuchUserException::new);
    }
}
