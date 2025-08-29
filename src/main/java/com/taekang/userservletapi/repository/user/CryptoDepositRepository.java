package com.taekang.userservletapi.repository.user;

import com.taekang.userservletapi.entity.user.CryptoDeposit;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CryptoDepositRepository extends JpaRepository<CryptoDeposit, Long> {

  Optional<CryptoDeposit> findById(Long id);

  boolean existsById(Long id);

  // 특정 지갑의 최근 입금 1건
  Optional<CryptoDeposit> findTopByCryptoAccount_CryptoWalletOrderByRequestedAtDesc(
      String cryptoWallet);
}
