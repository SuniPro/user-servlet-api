package com.taekang.userservletapi.error;

public class FailedMessageSendException extends BusinessException {
    public FailedMessageSendException() {
        super(ErrorCode.FAILED_MASSAGE_SEND);
    }
}
