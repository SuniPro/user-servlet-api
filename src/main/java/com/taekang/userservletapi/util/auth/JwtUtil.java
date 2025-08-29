package com.taekang.userservletapi.util.auth;

import com.taekang.userservletapi.DTO.crypto.CryptoAccountDTO;
import com.taekang.userservletapi.error.TokenNotValidateException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Slf4j
@PropertySource("classpath:application.properties")
@Component
public class JwtUtil {

  private final Key key;

  @Getter private final long accessTokenExpTime;
  @Getter private final long refreshTokenExpTime;

  public JwtUtil(
          @Value("${jwt.secret}") String secretKey,
          @Value("${jwt.access_token.expiration_time}") long accessTokenExpTime,
          @Value("${jwt.refresh_token.expiration_time}") long refreshTokenExpTime) {
    this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    this.accessTokenExpTime = accessTokenExpTime;
    this.refreshTokenExpTime = refreshTokenExpTime;
  }

  /**
   * Access Token 생성
   * @param cryptoAccountDTO CryptoAccountDTO
   * @return Access Token String
   */
  public String createAccessToken(CryptoAccountDTO cryptoAccountDTO) {
    return createToken(cryptoAccountDTO, accessTokenExpTime, "access-token");
  }

  /** Refresh Token 생성 */
  public String createRefreshToken(CryptoAccountDTO cryptoAccountDTO) {
    return createToken(cryptoAccountDTO, refreshTokenExpTime, "refresh-token");
  }

  /**
   * JWT 생성
   *
   * @param cryptoAccountDTO : 로직 내부에서 인증정보를 저장해둘 dto 입니다.
   * @param expireTime : 토큰의 만료시간입니다.
   * @return JWT String
   */
  private String createToken(CryptoAccountDTO cryptoAccountDTO, long expireTime, String type) {
    Claims claims = Jwts.claims();
    claims.put("email", cryptoAccountDTO.getEmail());
    claims.put("cryptoWallet", cryptoAccountDTO.getCryptoWallet());
    claims.put("site", cryptoAccountDTO.getSite());
    claims.put("chainType", cryptoAccountDTO.getChainType());
    claims.put("type", type); // 토큰 종류를 claim에 명시

    ZonedDateTime now = ZonedDateTime.now();
    ZonedDateTime validUntil = now.plusSeconds(expireTime);

    return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(Date.from(now.toInstant()))
            .setExpiration(Date.from(validUntil.toInstant()))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
  }

  /**
   * Token 에서 User ID 추출
   *
   * @param token : 발급된 토큰입니다.
   * @return User ID
   */
  public String getUserEmail(String token) throws TokenNotValidateException {
    return parseClaims(token).get("email", String.class);
  }

  public String getUserCryptoWallet(String token) throws TokenNotValidateException {
    return parseClaims(token).get("cryptoWallet", String.class);
  }

  public String getUserSite(String token) throws TokenNotValidateException {
    return parseClaims(token).get("site", String.class);
  }

  public String getUserChainType(String token) throws TokenNotValidateException {
    return parseClaims(token).get("chainType", String.class);
  }

  /**
   * JWT 검증
   *
   * @param token : 발급된 토큰입니다.
   * @return IsValidate
   */
  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    } catch (SecurityException | MalformedJwtException e) {
      log.info("Invalid JWT Token", e);
    } catch (ExpiredJwtException e) {
      log.info("Expired JWT Token", e);
    } catch (UnsupportedJwtException e) {
      log.info("Unsupported JWT Token", e);
    } catch (IllegalArgumentException e) {
      log.info("JWT claims string is empty.", e);
    }
    return false;
  }

  /**
   * JWT Claims 추출
   *
   * @param accessToken : 발급된 엑세스토큰입니다.
   * @return JWT Claims
   */
  public Claims parseClaims(String accessToken) {
    try {
      return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }
}
