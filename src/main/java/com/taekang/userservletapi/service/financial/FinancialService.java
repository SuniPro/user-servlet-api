package com.taekang.userservletapi.service.financial;

import java.math.BigDecimal;

public interface FinancialService {

  public void deposit(Long userId, BigDecimal amount);

  public void withDraw(Long userId, BigDecimal amount);

  public BigDecimal getBalance(Long userId);

  public void betOnSports(Long userId, BigDecimal amount);

  public void betOnCasino(Long userId, BigDecimal amount);
}
