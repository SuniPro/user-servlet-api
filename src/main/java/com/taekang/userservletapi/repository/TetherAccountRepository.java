package com.taekang.userservletapi.repository;

import com.taekang.userservletapi.entity.TetherAccount;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TetherAccountRepository extends JpaRepository<TetherAccount, Long> {

  Optional<TetherAccount> findByEmail(String email);

  Optional<TetherAccount> findByTetherWallet(String tetherWallet);

  Optional<TetherAccount> findByTetherWalletOrEmail(String tetherWallet, String email);

  boolean existsByEmail(String email);

  boolean existsByTetherWallet(String tetherWallet);

  boolean existsByTetherWalletAndEmail(String tetherWallet, String email);
}
