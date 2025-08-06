package com.taekang.userservletapi.DTO;

import lombok.*;

@Getter
@Setter
@ToString
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TokenDTO {

    private String accessToken;

    private String refreshToken;
}
