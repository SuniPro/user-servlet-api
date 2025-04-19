package com.taekang.userservletapi.repository;

import com.taekang.userservletapi.entity.TetherAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TetherAccountRepository extends JpaRepository<TetherAccount, Long> {

  Optional<TetherAccount> findByUsername(String username);

  Optional<TetherAccount> findByTetherWallet(String tetherWallet);

  Optional<TetherAccount> findByTetherWalletOrUsername(String tetherWallet, String username);

  boolean existsByUsername(String username);

  boolean existsByTetherWallet(String tetherWallet);

  boolean existsByTetherWalletAndUsername(String tetherWallet, String username);
}
