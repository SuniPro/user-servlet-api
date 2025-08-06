package com.taekang.userservletapi.error;

public class ValidationFailed extends BusinessException {
  public ValidationFailed() {
    super(ErrorCode.VALIDATION_FAILED);
  }
}
