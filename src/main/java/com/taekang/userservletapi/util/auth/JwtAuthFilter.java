package com.taekang.userservletapi.util.auth;

import com.taekang.userservletapi.DTO.crypto.CryptoAccountDTO;
import com.taekang.userservletapi.service.auth.CustomUserDetails;
import com.taekang.userservletapi.service.auth.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

  private final CustomUserDetailsService customUserDetailsService;

  private final JwtUtil jwtUtil;

  private final String[] authWhitelist; // 1. Whitelist를 받을 필드 추가
  private final AntPathMatcher pathMatcher = new AntPathMatcher();

  public JwtAuthFilter(
      CustomUserDetailsService customUserDetailsService,
      JwtUtil jwtUtil,
      @Value("${jwt.auth.whitelist}") String[] authWhitelist) {
    this.customUserDetailsService = customUserDetailsService;
    this.jwtUtil = jwtUtil;
    this.authWhitelist = authWhitelist;
  }

  /*
   * JWT 토큰 검증 필터 수행
   */
  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    // 2. 현재 요청 경로가 Whitelist에 있는지 확인
    String requestURI = request.getRequestURI();
    boolean isWhitelisted =
        Arrays.stream(authWhitelist).anyMatch(pattern -> pathMatcher.match(pattern, requestURI));

    // 3. Whitelist에 있다면, 토큰 검사를 생략하고 다음 필터로 바로 이동
    if (isWhitelisted) {
      filterChain.doFilter(request, response);
      return;
    }

    String token = null;
    // 1. 요청의 쿠키들을 가져옵니다.
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      // 2. 'accessToken'이라는 이름의 쿠키를 찾습니다. (이름은 서버에서 설정한 것과 동일해야 함)
      for (Cookie cookie : cookies) {
        if ("accessToken".equals(cookie.getName())) {
          token = cookie.getValue();
          break;
        }
      }
    }

    if (token != null) {
      if (jwtUtil.validateToken(token)) {
        String email = jwtUtil.getUserEmail(token);

        CryptoAccountDTO cryptoAccountDTO = customUserDetailsService.loadUserByEmail(email);

        if (cryptoAccountDTO != null) {
          UserDetails userDetails = new CustomUserDetails(cryptoAccountDTO);

          UsernamePasswordAuthenticationToken authentication =
              new UsernamePasswordAuthenticationToken(
                  userDetails, null, userDetails.getAuthorities());

          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      }
    }

    filterChain.doFilter(request, response); // 다음 필터로 넘기기
  }
}
