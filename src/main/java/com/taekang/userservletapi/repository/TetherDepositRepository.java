package com.taekang.userservletapi.repository;

import com.taekang.userservletapi.entity.TetherAccount;
import com.taekang.userservletapi.entity.TetherDeposit;
import com.taekang.userservletapi.entity.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TetherDepositRepository extends JpaRepository<TetherDeposit, Long> {

    Optional<TetherDeposit> findById(Long id);

    // 2. 계정별 전체 입금 조회 (마이페이지)
    List<TetherDeposit> findByTetherAccount_IdOrderByRequestedAtDesc(Long accountId);

    // 3. 상태 기반 조회 (예: PENDING 승인 대기 리스트)
    List<TetherDeposit> findByStatus(TransactionStatus status);

    // 4. 계정별 + 상태 필터 조회
    List<TetherDeposit> findByTetherAccount_IdAndStatus(Long accountId, TransactionStatus status);

    // 5. 최근 입금 하나
    Optional<TetherDeposit> findTopByTetherAccountOrderByRequestedAtDesc(TetherAccount account);

    // 6. 입금 날짜 기준 범위 조회 (통계/필터링)
    List<TetherDeposit> findByRequestedAtBetween(LocalDateTime start, LocalDateTime end);

    // 7. 상태별 총 입금 합계 (native)
    @Query(value = """
                SELECT COALESCE(SUM(amount), 0)
                FROM tether_deposit
                WHERE status = :status
            """, nativeQuery = true)
    BigDecimal sumByStatus(@Param("status") String status); // 또는 Enum → .name()으로 변환

    // 8. 중복 검사용 ID + 상태 체크
    Optional<TetherDeposit> findByIdAndStatus(Long id, TransactionStatus status);

    @Query("""
              SELECT DATE(d.requestedAt), SUM(d.amount)
              FROM TetherDeposit d
              WHERE d.requestedAt BETWEEN :start AND :end
              AND d.status = :status
              GROUP BY DATE(d.requestedAt)
              ORDER BY DATE(d.requestedAt)
            """)
    List<Object[]> getDailyTotals(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("status") TransactionStatus status);

    // 상태에 따른 특정 지갑 기준 입금 조회
    List<TetherDeposit> findByTetherAccount_TetherWalletAndStatus(String tetherWallet, TransactionStatus status);

    // 특정 지갑의 최근 입금 1건
    Optional<TetherDeposit> findTopByTetherAccount_TetherWalletOrderByRequestedAtDesc(String tetherWallet);

    // 특정 지갑의 입금 목록 (기간 기준)
    List<TetherDeposit> findByTetherAccount_TetherWalletAndRequestedAtBetween(String tetherWallet, LocalDateTime start, LocalDateTime end);

    @Query(value = """
                SELECT COALESCE(SUM(amount), 0)
                FROM tether_deposit
                WHERE status = :status
                  AND tether_account_id = (
                      SELECT id FROM tether_account WHERE tether_wallet = :wallet
                  )
            """, nativeQuery = true)
    BigDecimal sumByStatusAndWallet(@Param("status") String status, @Param("wallet") String tetherWallet);
}
