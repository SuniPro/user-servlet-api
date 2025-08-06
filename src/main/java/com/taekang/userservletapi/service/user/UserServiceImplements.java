package com.taekang.userservletapi.service.user;

import com.taekang.userservletapi.DTO.user.*;
import com.taekang.userservletapi.entity.RoleType;
import com.taekang.userservletapi.entity.User;
import com.taekang.userservletapi.error.AlreadyExistUserEmailException;
import com.taekang.userservletapi.error.AlreadyExistUsernameException;
import com.taekang.userservletapi.error.CannotFoundUserException;
import com.taekang.userservletapi.error.PasswordIncorrectException;
import com.taekang.userservletapi.repository.UserRepository;
import com.taekang.userservletapi.util.auth.JwtUtil;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImplements implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final ModelMapper modelMapper;

  private final JwtUtil jwtUtil;

  @Autowired
  public UserServiceImplements(
      UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      ModelMapper modelMapper,
      JwtUtil jwtUtil) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.modelMapper = modelMapper;
    this.jwtUtil = jwtUtil;
  }

  @Override
  @Transactional
  public UserDTO createUser(@Valid UserCreateRequestDTO userCreateRequestDTO) {
    if (userRepository.existsByEmail(userCreateRequestDTO.getEmail())) {
      throw new AlreadyExistUserEmailException();
    }

    if (userRepository.existsByUsername(userCreateRequestDTO.getUsername())) {
      throw new AlreadyExistUsernameException();
    }

    User user =
        User.builder()
            .email(userCreateRequestDTO.getEmail())
            .username(userCreateRequestDTO.getUsername())
            .password(passwordEncoder.encode(userCreateRequestDTO.getPassword()))
            .roleType(RoleType.USER)
            .isBlock(false)
            .build();

    return modelMapper.map(userRepository.save(user), UserDTO.class);
  }

  @Override
  public Page<UserDTO> getAllUser() {
    return null;
  }

  @Override
  public UserDTO getUserById(Long userId) {
    return null;
  }

  @Override
  public UserDTO updateUser(Long userId, UserCreateRequestDTO request) {
    return null;
  }

  @Override
  @Transactional
  public UserDTO passwordChange(UserDTO userDTO, PasswordChangeDTO passwordChangeDTO) {
    if (userRepository.existsByEmail(userDTO.getEmail())) {
      throw new AlreadyExistUserEmailException();
    }

    if (!passwordEncoder.matches(
        passwordChangeDTO.getOldPassword(),
        userRepository
            .findByEmail(userDTO.getEmail())
            .orElseThrow(CannotFoundUserException::new)
            .getPassword())) {
      throw new PasswordIncorrectException();
    }

    User user =
        User.builder()
            .id(userDTO.getId())
            .password(passwordEncoder.encode(passwordChangeDTO.getNewPassword()))
            .build();

    return modelMapper.map(user, UserDTO.class);
  }

  @Override
  public void deleteUser(Long userId) {}
}
