package com.taekang.userservletapi.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(WalletVerification.class)
  public ResponseEntity<ErrorResponse> handleWalletVerification(WalletVerification e) {

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponse(ErrorCode.WALLET_VERIFICATION));
  }

  @ExceptionHandler(DuplicateAccountException.class)
  public ResponseEntity<ErrorResponse> handleDuplicateAccount(DuplicateAccountException e) {
    return ResponseEntity.status(e.getErrorCode().getHttpStatus())
        .body(new ErrorResponse(ErrorCode.DUPLICATE_ACCOUNT));
  }

  @ExceptionHandler(AccountNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleAccountNotFound(AccountNotFoundException e) {

    return ResponseEntity.status(e.getErrorCode().getHttpStatus())
        .body(new ErrorResponse(e.getErrorCode()));
  }

  @ExceptionHandler(DepositNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleDepositNotFound(DepositNotFoundException e) {
    return ResponseEntity.status(e.getErrorCode().getHttpStatus())
        .body(new ErrorResponse(e.getErrorCode()));
  }

  // 다른 커스텀 예외들도 이렇게 추가하면 됨
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleOther(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR));
  }
}
