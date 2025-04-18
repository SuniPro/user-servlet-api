package com.taekang.userservletapi.DTO.tether;

import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TetherCreateDTO {

  private String username;

  private String tetherWallet;
}
