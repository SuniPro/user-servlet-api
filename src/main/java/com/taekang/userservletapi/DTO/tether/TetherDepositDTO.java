package com.taekang.userservletapi.DTO.tether;

import com.taekang.userservletapi.entity.TransactionStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TetherDepositDTO {

  private Long id;

  private String tetherWallet;

  private String email;

  private String virtual_wallet;

  private LocalDateTime insertDateTime;

  private String transactionHash;

  private BigDecimal amount;

  private BigDecimal usdtAmount;

  private Boolean accepted;

  private LocalDateTime acceptedAt;

  private LocalDateTime requestedAt;

  private TransactionStatus status;
}
