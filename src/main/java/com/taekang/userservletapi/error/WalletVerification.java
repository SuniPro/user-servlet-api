package com.taekang.userservletapi.error;

public class WalletVerification extends BusinessException {
  public WalletVerification() {
    super(ErrorCode.WALLET_VERIFICATION);
  }
}
