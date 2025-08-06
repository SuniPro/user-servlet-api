package com.taekang.userservletapi.error;

public class AlreadyExistUsernameException extends BusinessException {
  public AlreadyExistUsernameException() {
    super(ErrorCode.ALREADY_EXIST_USERNAME);
  }
}
