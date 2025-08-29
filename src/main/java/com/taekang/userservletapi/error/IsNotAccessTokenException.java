package com.taekang.userservletapi.error;

public class IsNotAccessTokenException extends BusinessException {
  public IsNotAccessTokenException() {
    super(ErrorCode.IS_NOT_ACCESS_TOKEN);
  }
}
