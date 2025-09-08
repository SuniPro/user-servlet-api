package com.taekang.userservletapi.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class TokenResponse {
  private final String accessToken;
  private final String refreshToken;
  private final long accessTokenExpiresIn;
  private final long refreshTokenExpiresIn;
}
