package com.taekang.userservletapi.service.financial.impl;

import com.taekang.userservletapi.DTO.tether.TetherAccountDTO;
import com.taekang.userservletapi.DTO.tether.TetherDepositRequestDTO;
import com.taekang.userservletapi.entity.TetherAccount;
import com.taekang.userservletapi.entity.TetherDeposit;
import com.taekang.userservletapi.entity.TetherWithdraw;
import com.taekang.userservletapi.entity.TransactionStatus;
import com.taekang.userservletapi.error.AccountNotFoundException;
import com.taekang.userservletapi.error.InvalidAmountException;
import com.taekang.userservletapi.repository.TetherAccountRepository;
import com.taekang.userservletapi.repository.TetherDepositRepository;
import com.taekang.userservletapi.repository.TetherWithdrawRepository;
import com.taekang.userservletapi.service.financial.TetherService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
  public TetherAccount createOrFindTetherAccount(TetherAccountDTO dto) {
    return tetherAccountRepository
        .findByTetherWallet(dto.getTetherWallet())
        .orElseGet(
            () -> {
              TetherAccount account =
                  TetherAccount.builder()
                      .username(dto.getUsername())
                      .tetherWallet(dto.getTetherWallet())
                      .insertDateTime(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
                      .build();
              return tetherAccountRepository.save(account);
            });
  }

  @Override
  @Transactional
  public TetherAccount updateTetherWallet(String tetherWallet) {
    TetherAccount tetherAccount =
        tetherAccountRepository
            .findByTetherWallet(tetherWallet)
            .orElseThrow(AccountNotFoundException::new);

    tetherAccount =
        tetherAccount.toBuilder()
            .tetherWallet(tetherWallet)
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
