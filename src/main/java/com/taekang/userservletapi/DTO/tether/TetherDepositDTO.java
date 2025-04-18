package com.taekang.userservletapi.DTO.tether;

import com.taekang.userservletapi.entity.TransactionStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TetherDepositDTO {

    private Long id;

    private String tetherWallet;

    private String username;

    private LocalDateTime insertDateTime;

    private BigDecimal amount;

    private Boolean accepted;

    private LocalDateTime acceptedAt;

    private LocalDateTime requestedAt;

    private TransactionStatus status;
}
