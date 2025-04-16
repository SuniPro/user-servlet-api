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
public class TetherDepositAcceptedDTO {

    private Long depositId;

    private String username; // 계정명 노출 목적

    private BigDecimal amount;

    private TransactionStatus status;

    private LocalDateTime acceptedAt;
}