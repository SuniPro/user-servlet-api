package com.taekang.userservletapi.DTO.crypto;

import com.taekang.userservletapi.entity.user.ChainType;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CryptoAccountDTO {

  private Long id;

  private String cryptoWallet;

  private ChainType chainType;

  private String email;

  private String site;

  private String memo;

  private LocalDateTime insertDateTime;

  private LocalDateTime updateDateTime;

  private LocalDateTime deleteDateTime;
}
