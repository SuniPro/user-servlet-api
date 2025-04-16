package com.taekang.userservletapi.DTO.tether;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TetherDepositRequestDTO {

    private String tetherWallet;

    private BigDecimal amount;
}
