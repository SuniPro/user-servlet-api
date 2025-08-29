package com.taekang.userservletapi.error;

public class CannotFoundTransferException extends BusinessException {
  public CannotFoundTransferException() {
    super(ErrorCode.CANNOT_FOUND_TRANSFER);
  }
}
