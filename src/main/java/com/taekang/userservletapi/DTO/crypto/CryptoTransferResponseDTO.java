package com.taekang.userservletapi.DTO.crypto;

import com.taekang.userservletapi.entity.user.ChainType;
import com.taekang.userservletapi.entity.user.CryptoType;
import com.taekang.userservletapi.entity.user.TransactionStatus;
import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CryptoTransferResponseDTO {

  private ChainType chainType;

  private CryptoType cryptoType;

  private String fromAddress;

  private String toAddress;

  private String cryptoAmount;

  private int decimals;

  private TransactionStatus status;

  private Boolean accepted;

  private Long acceptedAt;

  private String memo;
}
