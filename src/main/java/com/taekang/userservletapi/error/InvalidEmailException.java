package com.taekang.userservletapi.error;

public class InvalidEmailException extends BusinessException {
  public InvalidEmailException() {
    super(ErrorCode.INVALID_EMAIL);
  }
}
