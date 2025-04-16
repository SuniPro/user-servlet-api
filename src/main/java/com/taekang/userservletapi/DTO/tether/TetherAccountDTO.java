package com.taekang.userservletapi.DTO.tether;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TetherAccountDTO {

    private Long id;

    private String tetherWallet;

    private String username;

    private LocalDateTime insertDateTime;

    private LocalDateTime updateDateTime;

    private LocalDateTime deleteDateTime;
}