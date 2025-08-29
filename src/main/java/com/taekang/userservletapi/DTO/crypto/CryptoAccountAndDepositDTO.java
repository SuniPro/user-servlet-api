package com.taekang.userservletapi.DTO.crypto;

import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CryptoAccountAndDepositDTO {

  private Long id;

  private String cryptoWallet;

  private String email;

  private Boolean accepted;

  private LocalDateTime acceptedAt;

  private LocalDateTime requestedAt;

  private Boolean isSend;

  private LocalDateTime insertDateTime;

  private LocalDateTime updateDateTime;

  private LocalDateTime deleteDateTime;
}
