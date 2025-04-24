package com.taekang.userservletapi.service.financial.impl;

import com.taekang.userservletapi.DTO.tether.*;
import com.taekang.userservletapi.entity.*;
import com.taekang.userservletapi.error.*;
import com.taekang.userservletapi.repository.TetherAccountRepository;
import com.taekang.userservletapi.repository.TetherDepositRepository;
import com.taekang.userservletapi.service.EmailAuthorizationService;
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

  private final EmailAuthorizationService emailAuthorizationService;

  @Autowired
  public TetherServiceImplements(
      TetherAccountRepository tetherAccountRepository,
      TetherDepositRepository tetherDepositRepository,
      EmailAuthorizationService emailAuthorizationService) {
    this.tetherAccountRepository = tetherAccountRepository;
    this.tetherDepositRepository = tetherDepositRepository;
    this.emailAuthorizationService = emailAuthorizationService;
  }

  @Override
  public TetherAccountDTO getTetherWallet(String email) {
    TetherAccount tetherAccount =
        tetherAccountRepository.findByEmail(email).orElseThrow(AccountNotFoundException::new);

    return TetherAccountDTO.builder()
        .id(tetherAccount.getId())
        .tetherWallet(tetherAccount.getTetherWallet())
        .email(tetherAccount.getEmail())
        .virtualWallet(tetherAccount.getVirtualWallet())
        .insertDateTime(tetherAccount.getInsertDateTime())
        .updateDateTime(tetherAccount.getUpdateDateTime())
        .deleteDateTime(tetherAccount.getDeleteDateTime())
        .build();
  }

  @Override
  @Transactional
  public TetherAccountAndDepositDTO createOrFindTetherAccount(TetherCreateDTO dto) {
    boolean existAccount =
        tetherAccountRepository.existsByTetherWalletAndEmail(dto.getTetherWallet(), dto.getEmail());
    boolean existsUsername = tetherAccountRepository.existsByEmail(dto.getEmail());
    boolean existsWallet = tetherAccountRepository.existsByTetherWallet(dto.getTetherWallet());

    TetherAccount account;
    Optional<TetherDeposit> latestDepositOpt;

    if (existAccount) {
      account =
          tetherAccountRepository
              .findByTetherWallet(dto.getTetherWallet())
              .orElseThrow(AccountNotFoundException::new);
      latestDepositOpt =
          tetherDepositRepository.findTopByTetherAccount_TetherWalletOrderByRequestedAtDesc(
              account.getTetherWallet());
      return toDto(account, latestDepositOpt);
    } else if (existsUsername || existsWallet) {
      throw new WalletVerification(); // 중복 but 일치하지 않음
    } else {

      try {
        emailAuthorizationService.sendAuthMail(dto.getEmail());
      } catch (Exception e) {
        throw new FailedMessageSendException();
      }

      // 신규 등록
      account =
          tetherAccountRepository.save(
              TetherAccount.builder()
                  .email(dto.getEmail())
                  .tetherWallet(dto.getTetherWallet())
                  .insertDateTime(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
                  .build());

      latestDepositOpt =
          tetherDepositRepository.findTopByTetherAccount_TetherWalletOrderByRequestedAtDesc(
              account.getTetherWallet());
      return toDto(account, latestDepositOpt);
    }
  }

  private TetherAccountAndDepositDTO toDto(
      TetherAccount account, Optional<TetherDeposit> latestDepositOpt) {
    return TetherAccountAndDepositDTO.builder()
        .id(account.getId())
        .tetherWallet(account.getTetherWallet())
        .email(account.getEmail())
        .accepted(latestDepositOpt.map(TetherBaseTransaction::getAccepted).orElse(null))
        .acceptedAt(latestDepositOpt.map(TetherBaseTransaction::getAcceptedAt).orElse(null))
        .requestedAt(latestDepositOpt.map(TetherBaseTransaction::getRequestedAt).orElse(null))
        .insertDateTime(account.getInsertDateTime())
        .updateDateTime(account.getUpdateDateTime())
        .deleteDateTime(account.getDeleteDateTime())
        .build();
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
            .usdtAmount(dto.getUsdtAmount())
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
        .email(deposit.getTetherAccount().getEmail())
        .insertDateTime(deposit.getTetherAccount().getInsertDateTime())
        .amount(deposit.getAmount())
        .usdtAmount(deposit.getUsdtAmount())
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
