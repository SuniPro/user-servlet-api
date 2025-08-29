package com.taekang.userservletapi.repository.user;

import com.taekang.userservletapi.entity.user.CryptoAccount;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CryptoAccountRepository extends JpaRepository<CryptoAccount, Long> {

  Optional<CryptoAccount> findByEmail(String email);

  Optional<CryptoAccount> findByCryptoWallet(String cryptoWallet);

  boolean existsByEmail(String email);

  boolean existsByCryptoWallet(String cryptoWallet);

  boolean existsByCryptoWalletAndEmail(String cryptoWallet, String email);
}
