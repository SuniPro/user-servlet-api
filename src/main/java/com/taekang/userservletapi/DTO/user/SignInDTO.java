package com.taekang.userservletapi.DTO.user;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignInDTO {

    private String email;

    private String password;
}
