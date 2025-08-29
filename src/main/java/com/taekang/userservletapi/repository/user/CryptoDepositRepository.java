package com.taekang.userservletapi.repository.user;

import com.taekang.userservletapi.entity.user.CryptoAccount;
import com.taekang.userservletapi.entity.user.CryptoDeposit;
import com.taekang.userservletapi.entity.user.TransactionStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CryptoDepositRepository extends JpaRepository<CryptoDeposit, Long> {

  Optional<CryptoDeposit> findById(Long id);

  boolean existsById(Long id);

  List<CryptoDeposit> findByCryptoAccount_IdOrderByRequestedAtDesc(Long accountId);

  // 3. 상태 기반 조회 (예: PENDING 승인 대기 리스트)
  List<CryptoDeposit> findByStatus(TransactionStatus status);

  // 4. 계정별 + 상태 필터 조회
  List<CryptoDeposit> findByCryptoAccount_IdAndStatus(Long accountId, TransactionStatus status);

  // 5. 최근 입금 하나
  Optional<CryptoDeposit> findTopByCryptoAccountOrderByRequestedAtDesc(CryptoAccount account);

  // 6. 입금 날짜 기준 범위 조회 (통계/필터링)
  List<CryptoDeposit> findByRequestedAtBetween(LocalDateTime start, LocalDateTime end);

  // 7. 상태별 총 입금 합계 (native)
  @Query(
      value =
          """
                SELECT COALESCE(SUM(amount), 0)
                FROM crypto_deposit
                WHERE status = :status
            """,
      nativeQuery = true)
  BigDecimal sumByStatus(@Param("status") String status); // 또는 Enum → .name()으로 변환

  // 8. 중복 검사용 ID + 상태 체크
  Optional<CryptoDeposit> findByIdAndStatus(Long id, TransactionStatus status);

  @Query(
      """
              SELECT DATE(d.requestedAt), SUM(d.amount)
              FROM CryptoDeposit d
              WHERE d.requestedAt BETWEEN :start AND :end
              AND d.status = :status
              GROUP BY DATE(d.requestedAt)
              ORDER BY DATE(d.requestedAt)
            """)
  List<Object[]> getDailyTotals(
      @Param("start") LocalDateTime start,
      @Param("end") LocalDateTime end,
      @Param("status") TransactionStatus status);

  // 상태에 따른 특정 지갑 기준 입금 조회
  List<CryptoDeposit> findByCryptoAccount_CryptoWalletAndStatus(
      String cryptoWallet, TransactionStatus status);

  // 특정 지갑의 최근 입금 1건
  Optional<CryptoDeposit> findTopByCryptoAccount_CryptoWalletOrderByRequestedAtDesc(
      String cryptoWallet);

  // 특정 지갑의 입금 목록 (기간 기준)
  List<CryptoDeposit> findByCryptoAccount_CryptoWalletAndRequestedAtBetween(
      String cryptoWallet, LocalDateTime start, LocalDateTime end);

  @Query(
      value =
          """
                SELECT COALESCE(SUM(amount), 0)
                FROM crypto_deposit
                WHERE status = :status
                  AND crypto_account_id = (
                      SELECT id FROM crypto_account WHERE crypto_wallet = :wallet
                  )
            """,
      nativeQuery = true)
  BigDecimal sumByStatusAndWallet(
      @Param("status") String status, @Param("wallet") String cryptoWallet);
}
