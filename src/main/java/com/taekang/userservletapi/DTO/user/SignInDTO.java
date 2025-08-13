package com.taekang.userservletapi.DTO.user;

import lombok.*;

@Setter
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class SignInDTO {

    private String email;

    private String password;
}
