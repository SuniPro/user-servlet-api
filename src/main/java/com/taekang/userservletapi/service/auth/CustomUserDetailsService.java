package com.taekang.userservletapi.service.auth;

import com.taekang.userservletapi.DTO.CustomUserDTO;
import com.taekang.userservletapi.entity.User;
import com.taekang.userservletapi.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    private final ModelMapper mapper;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository, ModelMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("해당하는 유저가 없습니다."));

        CustomUserDTO DTO = mapper.map(user, CustomUserDTO.class);

        return new CustomUserDetails(DTO);
    }
}

