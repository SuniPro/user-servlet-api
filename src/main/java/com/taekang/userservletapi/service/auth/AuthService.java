package com.taekang.userservletapi.service.auth;

import com.taekang.userservletapi.DTO.TokenResponse;
import com.taekang.userservletapi.DTO.crypto.CryptoAccountDTO;
import com.taekang.userservletapi.DTO.crypto.CryptoCreateDTO;
import com.taekang.userservletapi.error.IsNotRefreshTokenException;
import com.taekang.userservletapi.error.TokenNotValidateException;
import com.taekang.userservletapi.util.auth.JwtUtil;
import io.jsonwebtoken.Claims;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

  private final JwtUtil jwtUtil;
  private final RedisTemplate<String, String> redisTemplate;
  private final CustomUserDetailsService customUserDetailsService;

  @Value("${app.redis.refresh-token-prefix:refresh:}")
  private String refreshTokenPrefix;

  @Autowired
  public AuthService(
      JwtUtil jwtUtil,
      RedisTemplate<String, String> redisTemplate,
      CustomUserDetailsService customUserDetailsService) {
    this.jwtUtil = jwtUtil;
    this.redisTemplate = redisTemplate;
    this.customUserDetailsService = customUserDetailsService;
  }

  /** 로그인 시: 1) 사용자 인증 2) Access/Refresh 토큰 생성 3) Redis에 Refresh 토큰 저장 4) TokenResponse 반환 */
  @Transactional
  public TokenResponse signIn(CryptoCreateDTO cryptoCreateDTO) {
    // — 1) UserDetails 로드(인증)
    CryptoAccountDTO cryptoAccountDTO =
        customUserDetailsService.loadUserByEmail(cryptoCreateDTO.getEmail());

    // — 3) 토큰 생성
    String accessToken = jwtUtil.createAccessToken(cryptoAccountDTO);
    String refreshToken = jwtUtil.createRefreshToken(cryptoAccountDTO);

    // — 4) Redis에 Refresh Token 저장 (key: refresh:{email})
    String key = refreshTokenPrefix + cryptoAccountDTO.getEmail();
    redisTemplate
        .opsForValue()
        .set(key, refreshToken, Duration.ofSeconds(jwtUtil.getRefreshTokenExpTime()));

    // — 5) 응답 DTO 반환
    return new TokenResponse(
        accessToken,
        refreshToken,
        jwtUtil.getAccessTokenExpTime(),
        jwtUtil.getRefreshTokenExpTime());
  }

  /** Refresh 토큰 재발급 */
  public TokenResponse refresh(String oldRefreshToken) {
    Claims claims = jwtUtil.parseClaims(oldRefreshToken);
    String s = jwtUtil.parseClaims(oldRefreshToken).get("type", String.class);
    // 1) 토큰 타입 검증
    if (!"refresh-token".equals(jwtUtil.parseClaims(oldRefreshToken).get("type", String.class))) {
      throw new IsNotRefreshTokenException();
    }

    // 2) 사용자 식별
    String email = jwtUtil.getUserEmail(oldRefreshToken);

    // 3) Redis 저장된 토큰 확인
    String key = refreshTokenPrefix + email;
    String saved = redisTemplate.opsForValue().get(key);
    if (saved == null || saved.equals(oldRefreshToken)) {
      throw new TokenNotValidateException();
    }

    // 4) 새 토큰 생성 & Redis 교체
    CryptoAccountDTO cryptoAccountDTO = customUserDetailsService.loadUserByEmail(email);
    String newAccess = jwtUtil.createAccessToken(cryptoAccountDTO);
    String newRefresh = jwtUtil.createRefreshToken(cryptoAccountDTO);
    redisTemplate
        .opsForValue()
        .set(key, newRefresh, Duration.ofSeconds(jwtUtil.getRefreshTokenExpTime()));

    return new TokenResponse(
        newAccess, newRefresh, jwtUtil.getAccessTokenExpTime(), jwtUtil.getRefreshTokenExpTime());
  }
}
