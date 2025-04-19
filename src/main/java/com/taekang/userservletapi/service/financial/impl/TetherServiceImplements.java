package com.taekang.userservletapi.service.financial.impl;

import com.taekang.userservletapi.DTO.tether.*;
import com.taekang.userservletapi.entity.*;
import com.taekang.userservletapi.error.AccountNotFoundException;
import com.taekang.userservletapi.error.DepositNotFoundException;
import com.taekang.userservletapi.error.InvalidAmountException;
import com.taekang.userservletapi.error.WalletVerification;
import com.taekang.userservletapi.repository.TetherAccountRepository;
import com.taekang.userservletapi.repository.TetherDepositRepository;
import com.taekang.userservletapi.service.financial.TetherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Slf4j
@Service
public class TetherServiceImplements implements TetherService {

    private final TetherAccountRepository tetherAccountRepository;

    private final TetherDepositRepository tetherDepositRepository;

    @Autowired
    public TetherServiceImplements(
            TetherAccountRepository tetherAccountRepository,
            TetherDepositRepository tetherDepositRepository) {
        this.tetherAccountRepository = tetherAccountRepository;
        this.tetherDepositRepository = tetherDepositRepository;
    }

    @Override
    @Transactional
    public TetherAccountAndDepositDTO createOrFindTetherAccount(TetherCreateDTO dto) {
        boolean existAccount =
                tetherAccountRepository.existsByTetherWalletAndUsername(
                        dto.getTetherWallet(), dto.getUsername());
        boolean existsUsername = tetherAccountRepository.existsByUsername(dto.getUsername());
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
            // 신규 등록
            account =
                    tetherAccountRepository.save(
                            TetherAccount.builder()
                                    .username(dto.getUsername())
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
                .username(account.getUsername())
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
