package com.taekang.userservletapi.error;

public class AccountNotFoundException extends BusinessException {
    public AccountNotFoundException() {
        super(ErrorCode.ACCOUNT_NOT_FOUND);
    }
}
