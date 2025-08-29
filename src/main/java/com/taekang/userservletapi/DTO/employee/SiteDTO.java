package com.taekang.userservletapi.DTO.employee;

import com.taekang.userservletapi.entity.user.ChainType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class SiteDTO {

    private Long id;

    private String site;

    private String cryptoWallet;
    private ChainType chainType;

    private LocalDateTime insertDateTime;
    private String insertId;
    private LocalDateTime updateDateTime;
    private String updateId;
    private LocalDateTime deleteDateTime;
    private String deleteId;
}
