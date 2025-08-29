package com.taekang.userservletapi.error;

public class IsNotSupportWalletTypeException extends BusinessException {
  public IsNotSupportWalletTypeException() {
    super(ErrorCode.IS_NOT_SUPPORT_WALLET_TYPE);
  }
}
