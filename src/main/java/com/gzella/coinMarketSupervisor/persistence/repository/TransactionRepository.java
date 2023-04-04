package com.gzella.coinMarketSupervisor.persistence.repository;

import com.gzella.coinMarketSupervisor.persistence.entity.Transaction;
import com.gzella.coinMarketSupervisor.persistence.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    Collection<Transaction> findAllByUser(User user);
}
