package com.taekang.userservletapi.error;

public class PasswordIncorrectException extends BusinessException {
  public PasswordIncorrectException() {
    super(ErrorCode.PASSWORD_INCORRECT);
  }
}
