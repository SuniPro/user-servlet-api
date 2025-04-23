package com.taekang.userservletapi.service.financial;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface ExchangeService {

    BigDecimal getExchangeInfo();
}
