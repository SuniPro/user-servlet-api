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
public class TetherDepositAcceptedDTO {

  private Long depositId;

  private String email;

  private BigDecimal amount;

  private TransactionStatus status;

  private LocalDateTime acceptedAt;
}
