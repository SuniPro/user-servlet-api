package com.taekang.userservletapi.DTO.crypto;

import java.math.BigDecimal;
import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TronTokenInfo {
    private String symbol;
    private String address;
    private BigDecimal decimals;
    private String name;
}
