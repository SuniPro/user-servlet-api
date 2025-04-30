package com.taekang.userservletapi.error;

public class AlreadyTransactionException extends BusinessException {
  public AlreadyTransactionException() {
    super(ErrorCode.ALREADY_TRANSACTION);
  }
}
