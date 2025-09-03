package com.taekang.userservletapi.controller;

import com.taekang.userservletapi.DTO.TokenResponse;
import com.taekang.userservletapi.DTO.crypto.*;
import com.taekang.userservletapi.entity.user.CryptoAccount;
import com.taekang.userservletapi.service.auth.AuthService;
import com.taekang.userservletapi.service.financial.CryptoService;
import com.taekang.userservletapi.util.auth.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("financial")
public class FinancialController {

  @Value("${server.servlet.context-path}")
  private String contextPath;

  @Value("${icoin.domain}")
  private String icoinDomain;

  private final CryptoService cryptoService;
  private final AuthService authService;
  private final JwtUtil jwtUtil;

  @Autowired
  public FinancialController(
      CryptoService cryptoService, AuthService authService, JwtUtil jwtUtil) {
    this.cryptoService = cryptoService;
    this.authService = authService;
    this.jwtUtil = jwtUtil;
  }

  @GetMapping("crypto/get/account/by/email/{email}")
  public ResponseEntity<CryptoAccountDTO> getCryptoAccountByEmail(@PathVariable String email) {
    return ResponseEntity.ok().body(cryptoService.getCryptoWallet(email));
  }

  @PutMapping("crypto/create")
  public ResponseEntity<CryptoAccountAndDepositDTO> createOrFindCryptoAccount(
      @RequestBody CryptoCreateDTO cryptoCreateDTO) {
    CryptoAccountAndDepositDTO orFindCryptoAccount =
        cryptoService.createOrFindCryptoAccount(cryptoCreateDTO);

    TokenResponse tokenResponse = authService.signIn(cryptoCreateDTO);

    ResponseCookie accessCookie =
        ResponseCookie.from("access-token", tokenResponse.getAccessToken())
            .httpOnly(true)
            //            .secure(true)
            .domain(icoinDomain)
            .path("/")
            .maxAge(tokenResponse.getAccessTokenExpiresIn())
            .sameSite("Strict")
            .build();

    // Refresh Token Cookie
    ResponseCookie refreshCookie =
        ResponseCookie.from("refresh-token", tokenResponse.getRefreshToken())
            .httpOnly(true)
            //            .secure(true)
            .domain(icoinDomain)
            .path("/")
            .maxAge(tokenResponse.getRefreshTokenExpiresIn())
            .sameSite("Strict")
            .build();

    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, accessCookie.toString(), refreshCookie.toString())
        .body(orFindCryptoAccount);
  }

  @PatchMapping("crypto/update/wallet")
  public ResponseEntity<CryptoAccount> updateCryptoWallet(
      @RequestBody CryptoWalletUpdateDTO cryptoWalletUpdateDTO) {
    return ResponseEntity.ok().body(cryptoService.updateCryptoWallet(cryptoWalletUpdateDTO));
  }

  @PostMapping("crypto/create/deposit")
  public ResponseEntity<CryptoDepositDTO> createCryptoDeposit(
      @RequestBody CryptoDepositRequestDTO cryptoDepositRequestDTO) {
    return ResponseEntity.ok().body(cryptoService.createDeposit(cryptoDepositRequestDTO));
  }

  @GetMapping("crypto/get/deposit/by/{id}")
  public ResponseEntity<CryptoDepositDTO> getDepositById(@PathVariable Long id) {
    return ResponseEntity.ok().body(cryptoService.getDepositById(id));
  }

  @GetMapping("crypto/get/deposit/by/crypto/wallet/{cryptoWallet}")
  public ResponseEntity<CryptoDepositDTO> getLatestDepositByWallet(
      @PathVariable String cryptoWallet) {
    return ResponseEntity.ok().body(cryptoService.getLatestDepositByCryptoWallet(cryptoWallet));
  }

  @PatchMapping("crypto/request/deposit")
  public ResponseEntity<CryptoDepositDTO> depositRequest(
      @CookieValue("access-token") String token, @RequestBody Long id) {
    String siteCode = jwtUtil.getUserSite(token);
    String cryptoWallet = jwtUtil.getUserCryptoWallet(token);
    return ResponseEntity.ok()
        .body(cryptoService.depositConfirmRequest(id, cryptoWallet, siteCode));
  }
}
