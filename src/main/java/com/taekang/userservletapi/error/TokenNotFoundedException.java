package com.taekang.userservletapi.error;

public class TokenNotFoundedException extends BusinessException {
  public TokenNotFoundedException() {
    super(ErrorCode.CANNOT_FIND_TOKEN);
  }
}
