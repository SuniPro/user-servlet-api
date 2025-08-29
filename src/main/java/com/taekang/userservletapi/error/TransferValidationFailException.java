package com.taekang.userservletapi.error;

public class TransferValidationFailException extends BusinessException {
  public TransferValidationFailException() {
    super(ErrorCode.TRANSFER_VALIDATION_FAIL);
  }
}
