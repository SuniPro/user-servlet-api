package com.taekang.userservletapi.DTO.user;

import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountInfoDTO {

  private String email;
  private String cryptoWallet;
  private String site;

  //    getUserChainType과 통일성을 위해 String으로 하였음.
  private String chainType;
}
