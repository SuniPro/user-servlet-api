package com.taekang.userservletapi.error;

public class AlreadyExistUserEmailException extends BusinessException {
  public AlreadyExistUserEmailException() {
    super(ErrorCode.ALREADY_EXIST_EMAIL);
  }
}
