package com.taekang.userservletapi.service.financial.impl;

import com.taekang.userservletapi.DTO.crypto.*;
import com.taekang.userservletapi.api.CryptoTransferValidation;
import com.taekang.userservletapi.api.CryptoValidationService;
import com.taekang.userservletapi.entity.user.*;
import com.taekang.userservletapi.error.*;
import com.taekang.userservletapi.rabbitMQ.MessageProducer;
import com.taekang.userservletapi.repository.user.CryptoAccountRepository;
import com.taekang.userservletapi.repository.user.CryptoDepositRepository;
import com.taekang.userservletapi.service.financial.CryptoService;
import com.taekang.userservletapi.util.WalletAddressType;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class CryptoServiceImpl implements CryptoService {

  private final CryptoAccountRepository cryptoAccountRepository;

  private final CryptoDepositRepository cryptoDepositRepository;

  private final CryptoTransferValidation cryptoTransferValidation;

  private final MessageProducer messageProducer;

  private final CryptoValidationService cryptoValidationService;
  private final ModelMapper modelMapper;

  @Autowired
  public CryptoServiceImpl(
      CryptoAccountRepository cryptoAccountRepository,
      CryptoDepositRepository cryptoDepositRepository,
      CryptoTransferValidation cryptoTransferValidation,
      MessageProducer messageProducer,
      CryptoValidationService cryptoValidationService,
      ModelMapper modelMapper) {
    this.cryptoAccountRepository = cryptoAccountRepository;
    this.cryptoDepositRepository = cryptoDepositRepository;
    this.cryptoTransferValidation = cryptoTransferValidation;
    this.messageProducer = messageProducer;
    this.cryptoValidationService = cryptoValidationService;
    this.modelMapper = modelMapper;
  }

  @Override
  public CryptoAccountDTO getCryptoWallet(String email) {
    CryptoAccount cryptoAccount =
        cryptoAccountRepository.findByEmail(email).orElseThrow(AccountNotFoundException::new);

    return CryptoAccountDTO.builder()
        .id(cryptoAccount.getId())
        .cryptoWallet(cryptoAccount.getCryptoWallet())
        .chainType(cryptoAccount.getChainType())
        .email(cryptoAccount.getEmail())
        .site(cryptoAccount.getSite())
        .insertDateTime(cryptoAccount.getInsertDateTime())
        .updateDateTime(cryptoAccount.getUpdateDateTime())
        .deleteDateTime(cryptoAccount.getDeleteDateTime())
        .build();
  }

  @Override
  @Transactional
  public CryptoAccountAndDepositDTO createOrFindCryptoAccount(CryptoCreateDTO dto) {
    boolean existAccount =
        cryptoAccountRepository.existsByCryptoWalletAndEmail(dto.getCryptoWallet(), dto.getEmail());
    boolean existsUsername = cryptoAccountRepository.existsByEmail(dto.getEmail());
    boolean existsWallet = cryptoAccountRepository.existsByCryptoWallet(dto.getCryptoWallet());

    CryptoAccount account;
    Optional<CryptoDeposit> latestDepositOpt;

    WalletAddressType type = WalletAddressType.of(dto.getCryptoWallet());

    if (!type.isInvalid(cryptoValidationService, dto.getCryptoWallet())) {
      throw new IsNotSupportWalletTypeException();
    }

    if (existAccount) {
      account =
          cryptoAccountRepository
              .findByCryptoWallet(dto.getCryptoWallet())
              .orElseThrow(AccountNotFoundException::new);
      latestDepositOpt =
          cryptoDepositRepository.findTopByCryptoAccount_CryptoWalletOrderByRequestedAtDesc(
              account.getCryptoWallet());
      return toDto(account, latestDepositOpt);
    } else if (existsUsername || existsWallet) {
      throw new WalletVerification(); // 중복 but 일치하지 않음
    } else {

      // 신규 등록
      account =
          cryptoAccountRepository.save(
              CryptoAccount.builder()
                  .cryptoWallet(dto.getCryptoWallet())
                  .chainType(type.toChainType())
                  .email(dto.getEmail())
                  .site(dto.getSite())
                  .insertDateTime(LocalDateTime.now())
                  .build());
      log.info("[createOrFindCryptoAccount] Account Created {}", account);

      latestDepositOpt =
          cryptoDepositRepository.findTopByCryptoAccount_CryptoWalletOrderByRequestedAtDesc(
              account.getCryptoWallet());

      log.info("[createOrFindCryptoAccount] Account latestDepositOpt {}", latestDepositOpt);
      return toDto(account, latestDepositOpt);
    }
  }

  private CryptoAccountAndDepositDTO toDto(
      CryptoAccount account, Optional<CryptoDeposit> latestDepositOpt) {
    return CryptoAccountAndDepositDTO.builder()
        .id(account.getId())
        .cryptoWallet(account.getCryptoWallet())
        .email(account.getEmail())
        .accepted(latestDepositOpt.map(CryptoBaseTransaction::getAccepted).orElse(null))
        .acceptedAt(latestDepositOpt.map(CryptoBaseTransaction::getAcceptedAt).orElse(null))
        .requestedAt(latestDepositOpt.map(CryptoBaseTransaction::getRequestedAt).orElse(null))
        .isSend(latestDepositOpt.map(CryptoBaseTransaction::isSend).orElse(false))
        .insertDateTime(account.getInsertDateTime())
        .updateDateTime(account.getUpdateDateTime())
        .deleteDateTime(account.getDeleteDateTime())
        .build();
  }

  @Override
  @Transactional
  public CryptoAccount updateCryptoWallet(CryptoWalletUpdateDTO cryptoWalletUpdateDTO) {
    CryptoAccount cryptoAccount =
        cryptoAccountRepository
            .findByCryptoWallet(cryptoWalletUpdateDTO.getCryptoWallet())
            .orElseThrow(AccountNotFoundException::new);

    cryptoAccount =
        cryptoAccount.toBuilder()
            .cryptoWallet(cryptoWalletUpdateDTO.getCryptoWallet())
            .updateDateTime(LocalDateTime.now())
            .build();
    return cryptoAccountRepository.save(cryptoAccount);
  }

  @Override
  @Transactional
  public CryptoDepositDTO createDeposit(CryptoDepositRequestDTO dto) {
    CryptoAccount cryptoAccount =
        cryptoAccountRepository
            .findByCryptoWallet(dto.getCryptoWallet())
            .orElseThrow(AccountNotFoundException::new);

    if (dto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
      throw new InvalidAmountException();
    }

    CryptoDeposit cryptoDeposit =
        CryptoDeposit.builder()
            .status(TransactionStatus.PENDING)
            .cryptoAccount(cryptoAccount)
            .chainType(cryptoAccount.getChainType())
            .cryptoType(dto.getCryptoType())
            .toAddress(dto.getToAddress())
            .fromAddress(dto.getFromAddress())
            .realAmount(null)
            .amount(dto.getAmount())
            .krwAmount(dto.getKrwAmount())
            .accepted(false)
            .requestedAt(LocalDateTime.now())
            .isSend(false)
            .build();

    return modelMapper.map(cryptoDepositRepository.save(cryptoDeposit), CryptoDepositDTO.class);
  }

  @Override
  @Transactional(readOnly = true)
  public CryptoDepositDTO getDepositById(Long id) {
    CryptoDeposit cryptoDeposit =
        cryptoDepositRepository.findById(id).orElseThrow(DepositNotFoundException::new);
    return modelMapper.map(cryptoDeposit, CryptoDepositDTO.class);
  }

  /** 특정 지갑의 마지막 입금 내역을 조회합니다. */
  @Override
  @Transactional(readOnly = true)
  public CryptoDepositDTO getLatestDepositByCryptoWallet(String cryptoWallet) {
    CryptoDeposit deposit =
        cryptoDepositRepository
            .findTopByCryptoAccount_CryptoWalletOrderByRequestedAtDesc(cryptoWallet)
            .orElseThrow(DepositNotFoundException::new);

    return toCryptoDepositDTO(deposit);
  }

  @Override
  @Transactional
  public CryptoDepositDTO depositConfirmRequest(Long id, String cryptoWallet, String siteCode) {

    CryptoAccount cryptoAccount =
        cryptoAccountRepository
            .findByCryptoWallet(cryptoWallet)
            .orElseThrow(AccountNotFoundException::new);
    CryptoDeposit cryptoDeposit =
        cryptoDepositRepository.findById(id).orElseThrow(DepositNotFoundException::new);

    CryptoTransferResponseDTO cryptoTransferResponseDTO;

    LocalDateTime ldt = cryptoDeposit.getRequestedAt();
    ZoneId origin = ZoneId.of("Asia/Seoul");
    long requestedAt = ldt.atZone(origin).toInstant().toEpochMilli();

    if (cryptoDeposit.getChainType().equals(ChainType.TRON)) {
      cryptoTransferResponseDTO =
          cryptoTransferValidation.TronTransferValidation(
              cryptoDeposit.getFromAddress(), cryptoDeposit.getToAddress(), requestedAt);
    } else {
      cryptoTransferResponseDTO =
          cryptoTransferValidation.EthTransferValidation(
              cryptoDeposit.getFromAddress(), cryptoDeposit.getToAddress(), requestedAt);
    }

    log.info("[TronWalletValidation] Response: {}", cryptoTransferResponseDTO.toString());

    CryptoDeposit build;
    BigDecimal responseAmount = new BigDecimal(cryptoTransferResponseDTO.getCryptoAmount());
    if (cryptoDeposit
            .getAmount()
            .compareTo(responseAmount.movePointLeft(cryptoTransferResponseDTO.getDecimals()))
        != 0) {
      build =
          cryptoDeposit.toBuilder()
              .status(TransactionStatus.FAILED)
              .realAmount(responseAmount)
              .accepted(false)
              .acceptedAt(null)
              .build();

    } else {
      long acceptedAtTimestamp = cryptoTransferResponseDTO.getAcceptedAt();
      Instant instant = Instant.ofEpochMilli(acceptedAtTimestamp);
      LocalDateTime acceptedAt = LocalDateTime.ofInstant(instant, ZoneId.of("Asia/Seoul"));

      build =
          cryptoDeposit.toBuilder()
              .status(cryptoTransferResponseDTO.getStatus())
              .realAmount(responseAmount)
              .accepted(true)
              .acceptedAt(acceptedAt)
              .acceptedAt(cryptoDeposit.getRequestedAt())
              .build();
    }

    CryptoDeposit save = cryptoDepositRepository.save(Objects.requireNonNull(build));

    CryptoDepositDTO result =
        CryptoDepositDTO.builder()
            .id(save.getId())
            .email(cryptoAccount.getEmail())
            .status(save.getStatus())
            .chainType(save.getChainType())
            .cryptoType(save.getCryptoType())
            .fromAddress(save.getFromAddress())
            .toAddress(save.getToAddress())
            .amount(save.getAmount())
            .krwAmount(save.getKrwAmount())
            .realAmount(save.getRealAmount())
            .accepted(save.getAccepted())
            .acceptedAt(save.getAcceptedAt())
            .requestedAt(save.getRequestedAt())
            .isSend(save.isSend())
            .build();

    DepositNotifyDTO depositNotifyDTO =
        DepositNotifyDTO.builder()
            .site(siteCode)
            .email(result.getEmail())
            .cryptoType(result.getCryptoType())
            .fromAddress(result.getFromAddress())
            .amount(result.getAmount())
            .krwAmount(result.getKrwAmount())
            .realAmount(result.getRealAmount())
            .requestedAt(result.getRequestedAt())
            .build();

    messageProducer.sendDepositMessage(depositNotifyDTO);

    return result;
  }

  private CryptoDepositDTO toCryptoDepositDTO(CryptoDeposit save) {
    return CryptoDepositDTO.builder()
        .id(save.getId())
        .email(save.getCryptoAccount().getEmail())
        .status(save.getStatus())
        .chainType(save.getChainType())
        .cryptoType(save.getCryptoType())
        .fromAddress(save.getCryptoAccount().getCryptoWallet())
        .toAddress(save.getToAddress())
        .amount(save.getAmount())
        .krwAmount(save.getKrwAmount())
        .realAmount(save.getRealAmount())
        .accepted(save.getAccepted())
        .acceptedAt(save.getAcceptedAt())
        .requestedAt(save.getRequestedAt())
        .isSend(save.isSend())
        .build();
  }
}
