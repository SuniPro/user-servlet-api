package com.taekang.userservletapi.config;

import com.taekang.userservletapi.service.auth.CustomUserDetailsService;
import com.taekang.userservletapi.util.auth.CustomAccessDeniedHandler;
import com.taekang.userservletapi.util.auth.CustomAuthenticationEntryPoint;
import com.taekang.userservletapi.util.auth.JwtAuthFilter;
import com.taekang.userservletapi.util.auth.JwtUtil;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
  private static final String[] AUTH_WHITELIST = {"/financial/tether/**", "/financial/exchange"};

  // CORS 허용경로
  private static final String[] CORS_WHITELIST = {"/user/**", "/board", "/file/get", "/tether/**"};

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // CSRF & CORS 설정
    http.csrf(AbstractHttpConfigurer::disable).cors(Customizer.withDefaults());

    // 세션 관리 (STATELESS: 세션을 사용하지 않음)
    http.sessionManagement(
        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    // 기본 로그인 방식(FormLogin, HttpBasic) 비활성화
    http.formLogin(AbstractHttpConfigurer::disable).httpBasic(AbstractHttpConfigurer::disable);

    // 예외 처리 핸들러
    http.exceptionHandling(
        ex ->
            ex.authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler));

    // JWT 인증 필터 추가 (UsernamePasswordAuthenticationFilter 앞에 배치)
    http.addFilterBefore(
        new JwtAuthFilter(customUserDetailsService, jwtUtil),
        UsernamePasswordAuthenticationFilter.class);

    // 인증·인가 규칙 (한 번만 선언!)
    http.authorizeHttpRequests(
        auth ->
            auth
                // 인증 없이 접근 허용
                .requestMatchers(AUTH_WHITELIST)
                .permitAll()
                // CORS에서 허용할 엔드포인트 (추가 설정 없으면 그냥 permitAll과 같음)
                .requestMatchers(CORS_WHITELIST)
                .permitAll()
                // 그 외는 무조건 인증
                .anyRequest()
                .authenticated());

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
    // 비밀키, iterationCount, hashWidth 설정
    Pbkdf2PasswordEncoder encoder =
        new Pbkdf2PasswordEncoder(
            "", 16, 10_000, Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);
    // Hmac SHA-256 사용 (기본은 SHA1)
    encoder.setAlgorithm(Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);
    return encoder;
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    // 허용할 프론트 도메인
    config.setAllowedOrigins(
        List.of("https://tie-ed.com", "https://icointext.com", "https://anycast.world"));

    // 허용 HTTP 메서드
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

    // 허용 헤더
    config.setAllowedHeaders(List.of("*"));

    // 자격증명(Cookie, Authorization 헤더) 허용
    config.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }
}
