package com.taekang.userservletapi.util.auth;

import com.taekang.userservletapi.service.auth.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

  private final CustomUserDetailsService customUserDetailsService;

  private final JwtUtil jwtUtil;

  /*
   * JWT 토큰 검증 필터 수행
   */
  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String authorizationHeader = request.getHeader("Authorization");

    String uri = request.getRequestURI();

    // ✅ financial 경로는 완전 스킵!
    if (uri.startsWith("/financial")) {
      log.info("Non Authorization Path: {}", uri);
      filterChain.doFilter(request, response);
      return;
    }

    // JWT가 헤더에 있는 경우
    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      String token = authorizationHeader.substring(7);
      // JWT 유효성 검증
      if (jwtUtil.validateToken(token)) {
        String username = jwtUtil.getUsername(token);

        // 유저와 토큰 일치 시 userDetails 생성
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        if (userDetails != null) {
          // UserDetsils, Password, Role -> 접근권한 인증 Token 생성
          UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
              new UsernamePasswordAuthenticationToken(
                  userDetails, null, userDetails.getAuthorities());

          // 현재 Request의 Security Context에 접근권한 설정
          SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
      }
    }

    filterChain.doFilter(request, response); // 다음 필터로 넘기기
  }
}
