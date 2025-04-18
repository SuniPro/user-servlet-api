package com.taekang.userservletapi.controller;

import com.taekang.userservletapi.DTO.tether.*;
import com.taekang.userservletapi.entity.TetherAccount;
import com.taekang.userservletapi.entity.TetherDeposit;
import com.taekang.userservletapi.service.financial.TetherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("financial")
public class FinancialController {

  private final TetherService tetherService;

  @Autowired
  public FinancialController(TetherService tetherService) {
    this.tetherService = tetherService;
  }

  @PutMapping("tether/create")
  public ResponseEntity<TetherAccountAndDepositDTO> createOrFindTetherAccount(
      @RequestBody TetherCreateDTO tetherCreateDTO) {
    return ResponseEntity.ok().body(tetherService.createOrFindTetherAccount(tetherCreateDTO));
  }

  @PatchMapping("tether/update/wallet")
  public ResponseEntity<TetherAccount> updateTetherWallet(
      @RequestBody TetherWalletUpdateDTO tetherWalletUpdateDTO) {
    return ResponseEntity.ok().body(tetherService.updateTetherWallet(tetherWalletUpdateDTO));
  }

  @PostMapping("tether/create/deposit")
  public ResponseEntity<TetherDeposit> createTetherDeposit(
      @RequestBody TetherDepositRequestDTO tetherDepositRequestDTO) {
    return ResponseEntity.ok().body(tetherService.createDeposit(tetherDepositRequestDTO));
  }

  @GetMapping("tether/get/deposit/by/tether/wallet/{tetherWallet}")
  public ResponseEntity<TetherDepositDTO> getLatestDepositByWallet(
      @PathVariable String tetherWallet) {
    return ResponseEntity.ok().body(tetherService.getLatestDepositByTetherWallet(tetherWallet));
  }
}
