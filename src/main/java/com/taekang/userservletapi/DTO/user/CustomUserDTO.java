package com.taekang.userservletapi.DTO.user;

import com.taekang.userservletapi.entity.RoleType;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomUserDTO extends UserDTO {

  private Long id;

  private String username;

  private RoleType roleType;

  private String email;

  private boolean isBlock;

  private LocalDateTime insertDateTime;

  private String insertId;

  private LocalDateTime updateDateTime;

  private String updateId;

  private LocalDateTime deleteDateTime;

  private String deleteId;
}
