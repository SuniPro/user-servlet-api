package com.taekang.userservletapi.service.financial;

import com.taekang.userservletapi.DTO.tether.*;
import com.taekang.userservletapi.entity.TetherAccount;
import com.taekang.userservletapi.entity.TetherDeposit;
import com.taekang.userservletapi.entity.TetherWithdraw;
import java.math.BigDecimal;

public interface TetherService {

  TetherAccountAndDepositDTO createOrFindTetherAccount(TetherCreateDTO tetherCreateDTO);

  TetherAccount updateTetherWallet(TetherWalletUpdateDTO tetherWalletUpdateDTO);

  TetherDeposit createDeposit(TetherDepositRequestDTO dto);

  TetherDepositDTO getLatestDepositByTetherWallet(String tetherWallet);

  TetherWithdraw withdrawInTetherWallet(String tetherWallet, BigDecimal amount);

  Boolean withdrawAccept(String tetherWallet, BigDecimal amount);
}
