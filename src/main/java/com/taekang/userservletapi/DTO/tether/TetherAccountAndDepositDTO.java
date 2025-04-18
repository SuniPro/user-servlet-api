package com.taekang.userservletapi.DTO.tether;

import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TetherAccountAndDepositDTO {

  private Long id;

  private String tetherWallet;

  private String username;

  private Boolean accepted;

  private LocalDateTime acceptedAt;

  private LocalDateTime requestedAt;

  private LocalDateTime insertDateTime;

  private LocalDateTime updateDateTime;

  private LocalDateTime deleteDateTime;
}
