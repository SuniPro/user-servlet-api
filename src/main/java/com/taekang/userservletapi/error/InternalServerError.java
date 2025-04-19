package com.taekang.userservletapi.error;

public class InternalServerError extends BusinessException {
    public InternalServerError() {
        super(ErrorCode.INTERNAL_SERVER_ERROR);
    }
}
