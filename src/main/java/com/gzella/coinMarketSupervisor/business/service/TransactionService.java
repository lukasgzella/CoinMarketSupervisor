package com.gzella.coinMarketSupervisor.business.service;

import com.gzella.coinMarketSupervisor.business.dto.TransactionDTO;
import com.gzella.coinMarketSupervisor.business.dto.TransactionFromDTOMapper;
import com.gzella.coinMarketSupervisor.business.dto.TransactionToDTOMapper;
import com.gzella.coinMarketSupervisor.persistence.entity.Transaction;
import com.gzella.coinMarketSupervisor.persistence.entity.User;
import com.gzella.coinMarketSupervisor.persistence.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final SecurityService securityService;
    private final TransactionRepository transactionRepository;
    private final TransactionFromDTOMapper transactionFromDTOMapper;
    private final TransactionToDTOMapper transactionToDTOMapper;

    public void registerNewTransaction(TransactionDTO transactionDTO) {
        User user = securityService.getAuthenticatedUser();
        Transaction transaction = transactionFromDTOMapper.apply(transactionDTO);
        transaction.setUser(user);
        transactionRepository.save(transaction);
    }

    public List<TransactionDTO> getTransactions() {
        User user = securityService.getAuthenticatedUser();
        return transactionRepository.findAllByUser(user)
                .stream()
                .map(transaction -> transactionToDTOMapper.apply(transaction))
                .collect(Collectors.toList());
    }
}