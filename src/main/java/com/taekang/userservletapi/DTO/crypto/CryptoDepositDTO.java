package com.taekang.userservletapi.DTO.crypto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.taekang.userservletapi.entity.user.ChainType;
import com.taekang.userservletapi.entity.user.CryptoType;
import com.taekang.userservletapi.entity.user.TransactionStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CryptoDepositDTO {

  private Long id;

  private String email;

  private TransactionStatus status;

  private ChainType chainType;

  private CryptoType cryptoType;

  private String fromAddress;

  private String toAddress;

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private BigDecimal amount;

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private BigDecimal krwAmount;

  private Boolean accepted;

  private LocalDateTime acceptedAt;

  private LocalDateTime requestedAt;

  @JsonProperty("isSend")
  private boolean isSend;

  private String memo;
}