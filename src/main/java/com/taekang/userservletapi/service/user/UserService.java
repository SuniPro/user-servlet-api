package com.taekang.userservletapi.service.user;

import com.taekang.userservletapi.DTO.user.UserCreateRequestDTO;
import com.taekang.userservletapi.entity.User;

public interface UserService {

  User findById(Long userId);

  User createUser(UserCreateRequestDTO request);

  void updateUser(Long userId, UserCreateRequestDTO request);

  void deleteUser(Long userId);
}
