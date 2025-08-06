package com.taekang.userservletapi.DTO.user;

import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserCreateRequestDTO {

  private String username;

  private String email;

  private String password;
}
