package com.gzella.coinMarketSupervisor.persistence.repository;

import com.gzella.coinMarketSupervisor.persistence.entity.WalletEntry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletEntryRepository extends CrudRepository<WalletEntry, Long> {

}
