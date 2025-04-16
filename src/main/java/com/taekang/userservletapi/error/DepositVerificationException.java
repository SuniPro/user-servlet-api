package com.taekang.userservletapi.error;

public class DepositVerificationException extends BusinessException {
    public DepositVerificationException() {
        super(ErrorCode.DEPOSIT_VERIFICATION);
    }
}
