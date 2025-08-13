package com.taekang.userservletapi.controller;

import com.taekang.userservletapi.DTO.TokenResponse;
import com.taekang.userservletapi.DTO.user.SignInDTO;
import com.taekang.userservletapi.DTO.user.UserCreateRequestDTO;
import com.taekang.userservletapi.DTO.user.UserDTO;
import com.taekang.userservletapi.service.auth.AuthService;
import com.taekang.userservletapi.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("user")
public class UserController {

  private final UserService userService;
  private final AuthService authService;

  @Value("${jwt.access_token.expiration_time}")
  private long expiration;

  @Autowired
  public UserController(UserService userService, AuthService authService) {
    this.userService = userService;
      this.authService = authService;
  }

  @PostMapping("create")
  public ResponseEntity<UserDTO> createUser(
      @RequestBody UserCreateRequestDTO userCreateRequestDTO) {
    UserDTO user = userService.createUser(userCreateRequestDTO);

    SignInDTO signInDTO = SignInDTO.builder()
            .email(userCreateRequestDTO.getEmail())
            .password(userCreateRequestDTO.getPassword())
            .build();
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
            .body(user);
  }
}
