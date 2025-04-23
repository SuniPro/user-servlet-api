package com.taekang.userservletapi.error;

public class CannotFoundedExchangeInfoException extends BusinessException {
  public CannotFoundedExchangeInfoException() {
    super(ErrorCode.CANNOT_FOUNDED_EXCHANGE_INFO);
  }
}
