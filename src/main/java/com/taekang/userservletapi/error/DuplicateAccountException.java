package com.taekang.userservletapi.error;

public class DuplicateAccountException extends BusinessException {
    public DuplicateAccountException() {
        super(ErrorCode.DUPLICATE_ACCOUNT);
    }
}
