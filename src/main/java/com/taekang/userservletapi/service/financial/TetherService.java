package com.taekang.userservletapi.service.financial;

import com.taekang.userservletapi.DTO.tether.TetherAccountDTO;
import com.taekang.userservletapi.DTO.tether.TetherDepositRequestDTO;
import com.taekang.userservletapi.entity.TetherAccount;
import com.taekang.userservletapi.entity.TetherDeposit;
import com.taekang.userservletapi.entity.TetherWithdraw;

import java.math.BigDecimal;

public interface TetherService {

    TetherAccount createOrFindTetherAccount(TetherAccountDTO tetherAccountDTO);

    TetherAccount updateTetherWallet(String tetherWallet);

    TetherDeposit createDeposit(TetherDepositRequestDTO dto);

    TetherWithdraw withdrawInTetherWallet(String tetherWallet, BigDecimal amount);

    Boolean withdrawAccept(String tetherWallet, BigDecimal amount);
}
