package com.taekang.userservletapi.config;

import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

@Configuration
public class JpaAuditingConfig {

  @Bean
  public AuditorAware<String> auditorProvider() {
    return () -> Optional.of("administrator");
  }
}
