package com.taekang.userservletapi.DTO.crypto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.taekang.userservletapi.entity.user.CryptoType;
import java.math.BigDecimal;
import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CryptoDepositRequestDTO {

  private String toAddress;

  private String fromAddress;

  private String cryptoWallet;

  private CryptoType cryptoType;

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private BigDecimal amount;

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private BigDecimal krwAmount;
}
