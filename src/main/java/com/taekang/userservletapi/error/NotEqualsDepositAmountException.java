package com.taekang.userservletapi.error;

public class NotEqualsDepositAmountException extends BusinessException {
  public NotEqualsDepositAmountException() {
    super(ErrorCode.NOT_EQUALS_DEPOSIT_AMOUNT);
  }
}
