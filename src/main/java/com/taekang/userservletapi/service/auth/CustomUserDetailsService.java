package com.taekang.userservletapi.service.auth;

import com.taekang.userservletapi.DTO.crypto.CryptoAccountDTO;
import com.taekang.userservletapi.entity.user.CryptoAccount;
import com.taekang.userservletapi.error.AccountNotFoundException;
import com.taekang.userservletapi.repository.user.CryptoAccountRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CustomUserDetailsService {

  private final CryptoAccountRepository cryptoAccountRepository;

  private final ModelMapper mapper;

  @Autowired
  public CustomUserDetailsService(CryptoAccountRepository cryptoAccountRepository, ModelMapper mapper) {
    this.cryptoAccountRepository = cryptoAccountRepository;
    this.mapper = mapper;
  }

  public CryptoAccountDTO loadUserByEmail(String email) throws AccountNotFoundException {
    CryptoAccount user =
        cryptoAccountRepository
                .findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저가 없습니다."));
    
    return mapper.map(user, CryptoAccountDTO.class);
  }

}
