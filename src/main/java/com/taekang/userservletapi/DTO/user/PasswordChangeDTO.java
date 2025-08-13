package com.taekang.userservletapi.DTO.user;

import lombok.*;

@Setter
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PasswordChangeDTO {

  private String email;

  private String oldPassword;

  private String newPassword;
}
