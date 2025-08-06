package com.taekang.userservletapi.service.user;

import com.taekang.userservletapi.DTO.user.PasswordChangeDTO;
import com.taekang.userservletapi.DTO.user.UserCreateRequestDTO;
import com.taekang.userservletapi.DTO.user.UserDTO;
import org.springframework.data.domain.Page;

public interface UserService {

  UserDTO createUser(UserCreateRequestDTO userCreateRequestDTO);

  UserDTO updateUser(Long userId, UserCreateRequestDTO request);

  UserDTO passwordChange(UserDTO userDTO, PasswordChangeDTO passwordChangeDTO);

  Page<UserDTO> getAllUser();

  UserDTO getUserById(Long userId);

  void deleteUser(Long userId);
}
