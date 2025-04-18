package com.taekang.userservletapi.service.financial.impl;

import com.taekang.userservletapi.DTO.tether.*;
import com.taekang.userservletapi.entity.*;
import com.taekang.userservletapi.error.AccountNotFoundException;
import com.taekang.userservletapi.error.DepositNotFoundException;
import com.taekang.userservletapi.error.InvalidAmountException;
import com.taekang.userservletapi.repository.TetherAccountRepository;
import com.taekang.userservletapi.repository.TetherDepositRepository;
import com.taekang.userservletapi.repository.TetherWithdrawRepository;
import com.taekang.userservletapi.service.financial.TetherService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class TetherServiceImplements implements TetherService {

  private final TetherAccountRepository tetherAccountRepository;

  private final TetherDepositRepository tetherDepositRepository;

  private final TetherWithdrawRepository tetherWithdrawRepository;

  @Autowired
  public TetherServiceImplements(
      TetherAccountRepository tetherAccountRepository,
      TetherDepositRepository tetherDepositRepository,
      TetherWithdrawRepository tetherWithdrawRepository) {
    this.tetherAccountRepository = tetherAccountRepository;
    this.tetherDepositRepository = tetherDepositRepository;
    this.tetherWithdrawRepository = tetherWithdrawRepository;
  }

  @Override
  @Transactional
  public TetherAccountAndDepositDTO createOrFindTetherAccount(TetherCreateDTO tetherCreateDTO) {
    TetherAccount account =
        tetherAccountRepository
            .findByTetherWallet(tetherCreateDTO.getTetherWallet())
            .orElseGet(
                () -> {
                  TetherAccount newAccount =
                      TetherAccount.builder()
                          .username(tetherCreateDTO.getUsername())
                          .tetherWallet(tetherCreateDTO.getTetherWallet())
                          .insertDateTime(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
                          .build();
                  return tetherAccountRepository.save(newAccount);
                });

    Optional<TetherDeposit> latestDepositOpt =
        tetherDepositRepository.findTopByTetherAccount_TetherWalletOrderByRequestedAtDesc(
            account.getTetherWallet());

    TetherAccountAndDepositDTO result =
        TetherAccountAndDepositDTO.builder()
            .id(account.getId())
            .tetherWallet(account.getTetherWallet())
            .username(account.getUsername())
            .accepted(latestDepositOpt.map(TetherBaseTransaction::getAccepted).orElse(null))
            .acceptedAt(latestDepositOpt.map(TetherBaseTransaction::getAcceptedAt).orElse(null))
            .requestedAt(latestDepositOpt.map(TetherBaseTransaction::getRequestedAt).orElse(null))
            .insertDateTime(account.getInsertDateTime())
            .updateDateTime(account.getUpdateDateTime())
            .deleteDateTime(account.getDeleteDateTime())
            .build();

    latestDepositOpt.ifPresent(
        deposit -> {
          result.setAccepted(deposit.getAccepted());
          result.setAcceptedAt(deposit.getAcceptedAt());
          result.setRequestedAt(deposit.getRequestedAt());
        });

    return result;
  }

  @Override
  @Transactional
  public TetherAccount updateTetherWallet(TetherWalletUpdateDTO tetherWalletUpdateDTO) {
    TetherAccount tetherAccount =
        tetherAccountRepository
            .findByTetherWallet(tetherWalletUpdateDTO.getTetherWallet())
            .orElseThrow(AccountNotFoundException::new);

    tetherAccount =
        tetherAccount.toBuilder()
            .tetherWallet(tetherWalletUpdateDTO.getTetherWallet())
            .updateDateTime(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
            .build();
    return tetherAccountRepository.save(tetherAccount);
  }

  @Override
  @Transactional
  public TetherDeposit createDeposit(TetherDepositRequestDTO dto) {
    TetherAccount tetherAccount =
        tetherAccountRepository
            .findByTetherWallet(dto.getTetherWallet())
            .orElseThrow(AccountNotFoundException::new);

    if (dto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
      throw new InvalidAmountException();
    }

    TetherDeposit tetherDeposit =
        TetherDeposit.builder()
            .tetherAccount(tetherAccount)
            .amount(dto.getAmount())
            .status(TransactionStatus.PENDING)
            .accepted(false)
            .requestedAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
            .build();

    return tetherDepositRepository.save(tetherDeposit);
  }

  /** 특정 지갑의 마지막 입금 내역을 조회합니다. */
  @Override
  @Transactional(readOnly = true)
  public TetherDepositDTO getLatestDepositByTetherWallet(String tetherWallet) {
    TetherDeposit deposit =
        tetherDepositRepository
            .findTopByTetherAccount_TetherWalletOrderByRequestedAtDesc(tetherWallet)
            .orElseThrow(DepositNotFoundException::new);

    return TetherDepositDTO.builder()
        .id(deposit.getId())
        .tetherWallet(deposit.getTetherAccount().getTetherWallet())
        .username(deposit.getTetherAccount().getUsername())
        .insertDateTime(deposit.getTetherAccount().getInsertDateTime())
        .amount(deposit.getAmount())
        .accepted(deposit.getAccepted())
        .acceptedAt(deposit.getAcceptedAt())
        .requestedAt(deposit.getRequestedAt())
        .status(deposit.getStatus())
        .build();
  }

  @Override
  @Transactional
  public TetherWithdraw withdrawInTetherWallet(String tetherWallet, BigDecimal amount) {
    return null;
  }

  @Override
  @Transactional
  public Boolean withdrawAccept(String tetherWallet, BigDecimal amount) {
    return null;
  }
}
