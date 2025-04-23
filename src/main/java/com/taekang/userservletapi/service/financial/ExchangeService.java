package com.taekang.userservletapi.service.financial;

import java.math.BigDecimal;
import org.springframework.stereotype.Service;

@Service
public interface ExchangeService {

  BigDecimal getExchangeInfo();
}
