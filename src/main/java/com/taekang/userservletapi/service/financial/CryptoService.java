package com.taekang.userservletapi.service.financial;

import com.taekang.userservletapi.DTO.crypto.*;
import com.taekang.userservletapi.entity.user.CryptoAccount;

public interface CryptoService {

  CryptoAccountDTO getCryptoWallet(String email);

  CryptoAccountAndDepositDTO createOrFindCryptoAccount(CryptoCreateDTO cryptoCreateDTO);

  CryptoAccount updateCryptoWallet(CryptoWalletUpdateDTO cryptoWalletUpdateDTO);

  CryptoDepositDTO createDeposit(CryptoDepositRequestDTO dto);

  CryptoDepositDTO getDepositById(Long id);

  CryptoDepositDTO getLatestDepositByCryptoWallet(String cryptoWallet);

  CryptoDepositDTO depositConfirmRequest(Long id, String cryptoWallet, String siteCode);
}
