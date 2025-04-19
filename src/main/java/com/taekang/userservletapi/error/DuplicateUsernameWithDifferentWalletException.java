package com.taekang.userservletapi.error;

public class DuplicateUsernameWithDifferentWalletException extends BusinessException {
  public DuplicateUsernameWithDifferentWalletException() {
    super(ErrorCode.DUPLICATE_USERNAME_WITH_DIFFERENT_WALLET);
  }
}
