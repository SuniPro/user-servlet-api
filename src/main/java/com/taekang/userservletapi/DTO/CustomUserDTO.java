package com.taekang.userservletapi.DTO;

import com.taekang.userservletapi.entity.RoleType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CustomUserDTO extends UserDTO {

    private String username;

    private String email;

    private String password;

    private RoleType roleType;

    private String profileImage;

    private String profileMessage;

    private String personalColor;

    private LocalDateTime insertDate;

    private String insertId;

    private LocalDateTime updateDate;

    private String updateId;

    private LocalDateTime deleteDate;

    private String deleteId;
}
