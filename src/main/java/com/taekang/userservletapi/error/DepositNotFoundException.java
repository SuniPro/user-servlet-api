package com.taekang.userservletapi.error;

public class DepositNotFoundException extends BusinessException {
  public DepositNotFoundException() {
    super(ErrorCode.DEPOSIT_NOT_FOUND);
  }
}
