package com.taekang.userservletapi.service.auth;

import com.taekang.userservletapi.DTO.crypto.CryptoAccountDTO;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public record CustomUserDetails(CryptoAccountDTO cryptoAccountDTO) implements UserDetails {

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    String site = cryptoAccountDTO.getSite(); // 예: "BINANCE"
    return List.of(new SimpleGrantedAuthority("SITE_" + site));
  }

  @Override
  public String getPassword() {
    return "";
  }

  @Override
  public String getUsername() {
    return "";
  }
}
