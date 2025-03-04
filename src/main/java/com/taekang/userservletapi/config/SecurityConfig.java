package com.taekang.userservletapi.config;

import com.taekang.userservletapi.service.auth.CustomUserDetailsService;
import com.taekang.userservletapi.util.auth.CustomAccessDeniedHandler;
import com.taekang.userservletapi.util.auth.CustomAuthenticationEntryPoint;
import com.taekang.userservletapi.util.auth.JwtAuthFilter;
import com.taekang.userservletapi.util.auth.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {

  private final CustomUserDetailsService customUserDetailsService;
  private final JwtUtil jwtUtil;
  private final CustomAccessDeniedHandler accessDeniedHandler;
  private final CustomAuthenticationEntryPoint authenticationEntryPoint;

  // 인증 없이 접근 가능한 URL 목록
  private static final String[] AUTH_WHITELIST = {
    "/user/**", "/board", "/file/get", "/collection/**"
  };

  // CORS 허용경로
  private static final String[] CORS_WHITELIST = {
    "/user/**", "/board", "/file/get", "/collection/**"
  };

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // CSRF & CORS 설정
    http.csrf(AbstractHttpConfigurer::disable) // CSRF 보호 비활성화 (JWT 사용 시 일반적으로 비활성화)
        .cors(Customizer.withDefaults()); // 기본 CORS 설정 적용

    // 세션 관리 (STATELESS: 세션을 사용하지 않음)
    http.sessionManagement(
        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    // 기본 로그인 방식(FormLogin, HttpBasic) 비활성화
    http.formLogin(AbstractHttpConfigurer::disable).httpBasic(AbstractHttpConfigurer::disable);

    // JWT 인증 필터 추가 (UsernamePasswordAuthenticationFilter 앞에 배치)
    http.addFilterBefore(
        new JwtAuthFilter(customUserDetailsService, jwtUtil),
        UsernamePasswordAuthenticationFilter.class);

    // 예외 처리 핸들러 설정
    http.exceptionHandling(
        exceptionHandling ->
            exceptionHandling
                .authenticationEntryPoint(authenticationEntryPoint) // 인증 실패 시
                .accessDeniedHandler(accessDeniedHandler) // 인가 실패 시
        );

    // 요청 인증 및 권한 설정
    http.authorizeHttpRequests(
        auth ->
            auth.requestMatchers(AUTH_WHITELIST)
                .permitAll() // 인증 없이 접근 허용
                .requestMatchers(CORS_WHITELIST)
                .permitAll() // CORS 경로도 인증 없이 허용
                .anyRequest()
                .authenticated() // 그 외 요청은 인증 필요
        );

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
  }
}
