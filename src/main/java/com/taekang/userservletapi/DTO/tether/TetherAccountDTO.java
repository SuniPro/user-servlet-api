package com.taekang.userservletapi.DTO.tether;

import java.time.LocalDateTime;
import lombok.*;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TetherAccountDTO {
    
    private Long id;

    private String tetherWallet;

    private String email;

    private String virtualWallet;

    private LocalDateTime insertDateTime;

    private LocalDateTime updateDateTime;

    private LocalDateTime deleteDateTime;
}
