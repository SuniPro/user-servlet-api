package com.taekang.userservletapi.controller;

import com.taekang.userservletapi.DTO.TokenResponse;
import com.taekang.userservletapi.DTO.user.SignInDTO;
import com.taekang.userservletapi.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/auth")
public class AuthController {

  private final AuthService authService;

  @Autowired
  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("login")
  public ResponseEntity<Void> login(@Valid @RequestBody SignInDTO signInDTO) {

    TokenResponse tokenResponse = authService.signIn(signInDTO);

    ResponseCookie accessCookie =
        ResponseCookie.from("access-token", tokenResponse.getAccessToken())
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(tokenResponse.getAccessTokenExpiresIn())
            .sameSite("Strict")
            .build();

    // Refresh Token Cookie
    ResponseCookie refreshCookie =
        ResponseCookie.from("refresh-token", tokenResponse.getRefreshToken())
            .httpOnly(true)
            .secure(true)
            .path("/user/auth/refresh")
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
                    .path("/")
                    .maxAge(tokens.getAccessTokenExpiresIn())
                    .sameSite("Strict")
                    .build();

    // 새 Refresh Token Cookie
    ResponseCookie refreshCookie =
            ResponseCookie.from("refresh-token", tokens.getRefreshToken())
                    .httpOnly(true)
                    .secure(true)
                    .path("/user/auth/refresh")
                    .maxAge(tokens.getRefreshTokenExpiresIn())
                    .sameSite("Strict")
                    .build();

    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, accessCookie.toString(), refreshCookie.toString())
        .build();
  }
}
