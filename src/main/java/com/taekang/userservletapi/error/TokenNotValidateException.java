package com.taekang.userservletapi.error;

public class TokenNotValidateException extends BusinessException {
  public TokenNotValidateException() {
    super(ErrorCode.TOKEN_NOT_VALIDATE);
  }
}
