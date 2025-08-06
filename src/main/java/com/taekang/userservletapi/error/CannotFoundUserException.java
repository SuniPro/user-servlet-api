package com.taekang.userservletapi.error;

public class CannotFoundUserException extends BusinessException {
  public CannotFoundUserException() {
    super(ErrorCode.CANNOT_FOUND_USER);
  }
}
