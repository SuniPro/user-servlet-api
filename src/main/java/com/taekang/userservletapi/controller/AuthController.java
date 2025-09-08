package com.taekang.userservletapi.controller;

import com.taekang.userservletapi.DTO.TokenResponse;
import com.taekang.userservletapi.DTO.crypto.CryptoCreateDTO;
import com.taekang.userservletapi.DTO.user.UserAccountInfoDTO;
import com.taekang.userservletapi.service.auth.AuthService;
import com.taekang.userservletapi.util.auth.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

  @Value("${server.servlet.context-path}")
  private String contextPath;

  @Value("${icoin.domain}")
  private String icoinDomain;

  private final AuthService authService;
  private final JwtUtil jwtUtil;

  @Autowired
  public AuthController(AuthService authService, JwtUtil jwtUtil) {
    this.authService = authService;
    this.jwtUtil = jwtUtil;
  }

  @GetMapping("check")
  public ResponseEntity<UserAccountInfoDTO> check(@CookieValue("access-token") String token) {
    String email = jwtUtil.getUserEmail(token);
    String wallet = jwtUtil.getUserCryptoWallet(token);
    String site = jwtUtil.getUserSite(token);
    String chainType = jwtUtil.getUserChainType(token);
    return ResponseEntity.ok(new UserAccountInfoDTO(email, wallet, site, chainType));
  }

  @PostMapping("login")
  public ResponseEntity<Void> login(@Valid @RequestBody CryptoCreateDTO cryptoCreateDTO) {

    TokenResponse tokenResponse = authService.signIn(cryptoCreateDTO);

    ResponseCookie accessCookie =
        ResponseCookie.from("access-token", tokenResponse.getAccessToken())
            .httpOnly(true)
            .secure(true)
            .domain(icoinDomain)
            .path(contextPath)
            .maxAge(tokenResponse.getAccessTokenExpiresIn())
            .sameSite("Strict")
            .build();

    // Refresh Token Cookie
    ResponseCookie refreshCookie =
        ResponseCookie.from("refresh-token", tokenResponse.getRefreshToken())
            .httpOnly(true)
            .secure(true)
            .domain(icoinDomain)
            .path(contextPath)
            .maxAge(tokenResponse.getRefreshTokenExpiresIn())
            .sameSite("Strict")
            .build();

    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, accessCookie.toString(), refreshCookie.toString())
        .build();
  }

  @PostMapping("/refresh")
  public ResponseEntity<Void> refresh(@CookieValue("refresh-token") String refreshToken) {
    TokenResponse tokens = authService.refresh(refreshToken);

    // 새 Access Token Cookie
    ResponseCookie accessCookie =
        ResponseCookie.from("access-token", tokens.getAccessToken())
            .httpOnly(true)
            .secure(true)
            .domain(icoinDomain)
                .path(contextPath)
            .maxAge(tokens.getAccessTokenExpiresIn())
            .sameSite("Strict")
            .build();

    // 새 Refresh Token Cookie
    ResponseCookie refreshCookie =
        ResponseCookie.from("refresh-token", tokens.getRefreshToken())
            .httpOnly(true)
            .secure(true)
            .domain(icoinDomain)
                .path(contextPath)
            .maxAge(tokens.getRefreshTokenExpiresIn())
            .sameSite("Strict")
            .build();

    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, accessCookie.toString(), refreshCookie.toString())
        .build();
  }
}
