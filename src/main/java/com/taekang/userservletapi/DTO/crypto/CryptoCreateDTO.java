package com.taekang.userservletapi.DTO.crypto;

import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CryptoCreateDTO {

  private String email;

  private String cryptoWallet;

  private String site;
}
