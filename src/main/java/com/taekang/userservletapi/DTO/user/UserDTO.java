package com.taekang.userservletapi.DTO.user;

import com.taekang.userservletapi.entity.RoleType;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

  private Integer id;

  private String username;

  private String phoneNumber;

  private String email;

  private String password;

  private RoleType roleType;

  private LocalDateTime insertDate;

  private String insertId;

  private LocalDateTime updateDate;

  private String updateId;

  private LocalDateTime deleteDate;

  private String deleteId;
}
