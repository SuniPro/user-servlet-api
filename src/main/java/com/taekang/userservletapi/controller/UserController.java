package com.taekang.userservletapi.controller;

import com.taekang.userservletapi.DTO.user.UserCreateRequestDTO;
import com.taekang.userservletapi.DTO.user.UserDTO;
import com.taekang.userservletapi.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("user")
public class UserController {

  private final UserService userService;

  @Value("${jwt.access_token.expiration_time}")
  private long expiration;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("create")
  public ResponseEntity<UserDTO> createUser(
      @RequestBody UserCreateRequestDTO userCreateRequestDTO) {
    return ResponseEntity.ok().body(userService.createUser(userCreateRequestDTO));
  }
}
