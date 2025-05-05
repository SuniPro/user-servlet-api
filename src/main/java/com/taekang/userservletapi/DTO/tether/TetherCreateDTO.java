package com.taekang.userservletapi.DTO.tether;

import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TetherCreateDTO {

  private String email;

  private String tetherWallet;

  private String site;
}
