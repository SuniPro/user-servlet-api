package com.taekang.userservletapi.service.auth;

import com.taekang.userservletapi.DTO.TokenResponse;
import com.taekang.userservletapi.DTO.user.CustomUserDTO;
import com.taekang.userservletapi.DTO.user.SignInDTO;
import com.taekang.userservletapi.entity.User;
import com.taekang.userservletapi.error.CannotFoundUserException;
import com.taekang.userservletapi.error.IsNotRefreshTokenException;
import com.taekang.userservletapi.error.PasswordIncorrectException;
import com.taekang.userservletapi.error.TokenNotValidateException;
import com.taekang.userservletapi.repository.UserRepository;
import com.taekang.userservletapi.util.auth.JwtUtil;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

  private final JwtUtil jwtUtil;
  private final RedisTemplate<String, String> redisTemplate;
  private final CustomUserDetailsService customUserDetailsService;
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;

  @Value("${app.redis.refresh-token-prefix:refresh:}")
  private String refreshTokenPrefix;

  @Autowired
  public AuthService(
      JwtUtil jwtUtil,
      RedisTemplate<String, String> redisTemplate,
      CustomUserDetailsService customUserDetailsService,
      PasswordEncoder passwordEncoder,
      UserRepository userRepository) {
    this.jwtUtil = jwtUtil;
    this.redisTemplate = redisTemplate;
    this.customUserDetailsService = customUserDetailsService;
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
  }

  /** 로그인 시: 1) 사용자 인증 2) Access/Refresh 토큰 생성 3) Redis에 Refresh 토큰 저장 4) TokenResponse 반환 */
  @Transactional
  public TokenResponse signIn(SignInDTO signInDTO) {
    // — 1) UserDetails 로드(인증)
    UserDetails userDetails = customUserDetailsService.loadUserByUsername(signInDTO.getEmail());
    User user =
        userRepository.findByEmail(signInDTO.getEmail()).orElseThrow(CannotFoundUserException::new);

    if (!passwordEncoder.matches(signInDTO.getPassword(), user.getPassword())) {
      throw new PasswordIncorrectException();
    }

    // — 2) CustomUserDTO 추출
    CustomUserDTO userDto = ((CustomUserDetails) userDetails).customUserDTO();

    // — 3) 토큰 생성
    String accessToken = jwtUtil.createAccessToken(userDto);
    String refreshToken = jwtUtil.createRefreshToken(userDto);

    // — 4) Redis에 Refresh Token 저장 (key: refresh:{email})
    String key = refreshTokenPrefix + userDto.getEmail();
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
    // 1) 토큰 타입 검증
    if (!"refresh".equals(jwtUtil.parseClaims(oldRefreshToken).get("type", String.class))) {
      throw new IsNotRefreshTokenException();
    }

    // 2) 사용자 식별
    String email = jwtUtil.getUserEmail(oldRefreshToken);

    // 3) Redis 저장된 토큰 확인
    String key = refreshTokenPrefix + email;
    String saved = redisTemplate.opsForValue().get(key);
    if (saved == null || !saved.equals(oldRefreshToken)) {
      throw new TokenNotValidateException();
    }

    // 4) 새 토큰 생성 & Redis 교체
    CustomUserDTO userDto = loadUserByEmail(email);
    String newAccess = jwtUtil.createAccessToken(userDto);
    String newRefresh = jwtUtil.createRefreshToken(userDto);
    redisTemplate
        .opsForValue()
        .set(key, newRefresh, Duration.ofSeconds(jwtUtil.getRefreshTokenExpTime()));

    return new TokenResponse(
        newAccess, newRefresh, jwtUtil.getAccessTokenExpTime(), jwtUtil.getRefreshTokenExpTime());
  }

  private CustomUserDTO loadUserByEmail(String email) {

    return customUserDetailsService.loadUserByEmail(email);
  }
}
