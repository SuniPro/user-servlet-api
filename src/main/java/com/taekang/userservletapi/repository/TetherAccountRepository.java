package com.taekang.userservletapi.repository;

import com.taekang.userservletapi.entity.TetherAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TetherAccountRepository extends JpaRepository<TetherAccount, Long> {

    Optional<TetherAccount> findByTetherWallet(String tetherWallet);
}
