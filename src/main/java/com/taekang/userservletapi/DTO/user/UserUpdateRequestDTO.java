package com.taekang.userservletapi.DTO.user;

import com.taekang.userservletapi.entity.RoleType;

public class UserUpdateRequestDTO {
                    private Long id;
    private String username;

    private String password;

    private RoleType roleType;

    private String phone;
}
