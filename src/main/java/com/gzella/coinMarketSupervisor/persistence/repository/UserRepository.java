package com.gzella.coinMarketSupervisor.persistence.repository;

import com.gzella.coinMarketSupervisor.persistence.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findUserByUsername(String username);

    boolean existsByUsername(String username);
}