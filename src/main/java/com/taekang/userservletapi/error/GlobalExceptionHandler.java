package com.taekang.userservletapi.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
    ErrorCode code = e.getErrorCode();
    return ResponseEntity.status(code.getHttpStatus()).body(new ErrorResponse(code));
  }

  /** DTO 검증 실패를 핸들링합니다. */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
    String detail =
        ex.getBindingResult().getFieldErrors().stream()
            .findFirst()
            .map(err -> err.getField() + ": " + err.getDefaultMessage())
            .orElse("Validation failed");
    ErrorResponse err = new ErrorResponse(ErrorCode.VALIDATION_FAILED, detail);
    return ResponseEntity.status(ErrorCode.VALIDATION_FAILED.getHttpStatus()).body(err);
  }

  @ExceptionHandler(IsNotAccessTokenException.class)
  public ResponseEntity<ErrorResponse> handleIsNotAccessTokenException(
      IsNotAccessTokenException e) {

    return ResponseEntity.status(e.getErrorCode().getHttpStatus())
        .body(new ErrorResponse(e.getErrorCode()));
  }

  @ExceptionHandler(IsNotRefreshTokenException.class)
  public ResponseEntity<ErrorResponse> handleIsNotRefreshTokenException(
      IsNotRefreshTokenException e) {

    return ResponseEntity.status(e.getErrorCode().getHttpStatus())
        .body(new ErrorResponse(e.getErrorCode()));
  }

  @ExceptionHandler(TokenNotValidateException.class)
  public ResponseEntity<ErrorResponse> handleTokenNotValidateException(
      TokenNotValidateException e) {

    return ResponseEntity.status(e.getErrorCode().getHttpStatus())
        .body(new ErrorResponse(e.getErrorCode()));
  }

  @ExceptionHandler(WalletVerification.class)
  public ResponseEntity<ErrorResponse> handleWalletVerification(WalletVerification e) {

    return ResponseEntity.status(e.getErrorCode().getHttpStatus())
        .body(new ErrorResponse(e.getErrorCode()));
  }

  @ExceptionHandler(DuplicateAccountException.class)
  public ResponseEntity<ErrorResponse> handleDuplicateAccount(DuplicateAccountException e) {
    return ResponseEntity.status(e.getErrorCode().getHttpStatus())
        .body(new ErrorResponse(e.getErrorCode()));
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

  @ExceptionHandler(InvalidEmailException.class)
  public ResponseEntity<ErrorResponse> handleInvalidEmail(InvalidEmailException e) {
    return ResponseEntity.status(e.getErrorCode().getHttpStatus())
        .body(new ErrorResponse(e.getErrorCode()));
  }

  @ExceptionHandler(FailedMessageSendException.class)
  public ResponseEntity<ErrorResponse> handleFailedMessageSendException(
      FailedMessageSendException e) {
    return ResponseEntity.status(e.getErrorCode().getHttpStatus())
        .body(new ErrorResponse(e.getErrorCode()));
  }

  @ExceptionHandler(CannotFoundUserException.class)
  public ResponseEntity<ErrorResponse> handleCannotFoundUserException(CannotFoundUserException e) {
    return ResponseEntity.status(e.getErrorCode().getHttpStatus())
        .body(new ErrorResponse(e.getErrorCode()));
  }

  @ExceptionHandler(AlreadyExistUserEmailException.class)
  public ResponseEntity<ErrorResponse> handleAlreadyExistUserEmailException(
      AlreadyExistUserEmailException e) {
    return ResponseEntity.status(e.getErrorCode().getHttpStatus())
        .body(new ErrorResponse(e.getErrorCode()));
  }

  @ExceptionHandler(AlreadyExistUsernameException.class)
  public ResponseEntity<ErrorResponse> handleAlreadyExistUsernameException(
      AlreadyExistUsernameException e) {
    return ResponseEntity.status(e.getErrorCode().getHttpStatus())
        .body(new ErrorResponse(e.getErrorCode()));
  }

  @ExceptionHandler(IsNotSupportWalletTypeException.class)
  public ResponseEntity<ErrorResponse> handleIsNotApplyWalletTypeException(
      IsNotSupportWalletTypeException e) {
    return ResponseEntity.status(e.getErrorCode().getHttpStatus())
        .body(new ErrorResponse(e.getErrorCode()));
  }

  @ExceptionHandler(NotEqualsDepositAmountException.class)
  public ResponseEntity<ErrorResponse> handleNotEqualsDepositAmountException(
      NotEqualsDepositAmountException e) {
    return ResponseEntity.status(e.getErrorCode().getHttpStatus())
        .body(new ErrorResponse(e.getErrorCode()));
  }

  @ExceptionHandler(TransferValidationFailException.class)
  public ResponseEntity<ErrorResponse> handleTransferValidationFail(
      TransferValidationFailException e) {
    return ResponseEntity.status(e.getErrorCode().getHttpStatus())
        .body(new ErrorResponse(e.getErrorCode()));
  }

  @ExceptionHandler(CannotFoundTransferException.class)
  public ResponseEntity<ErrorResponse> handleCannotFoundTransferException(
      CannotFoundTransferException e) {
    return ResponseEntity.status(e.getErrorCode().getHttpStatus())
        .body(new ErrorResponse(e.getErrorCode()));
  }

  @ExceptionHandler(CannotFoundSiteException.class)
  public ResponseEntity<ErrorResponse> handleCannotFoundSiteException(CannotFoundSiteException e) {
    return ResponseEntity.status(e.getErrorCode().getHttpStatus())
        .body(new ErrorResponse(e.getErrorCode()));
  }

  //  // 다른 커스텀 예외들도 이렇게 추가하면 됨
  //  @ExceptionHandler(Exception.class)
  //  public ResponseEntity<ErrorResponse> handleOther(Exception ex) {
  //    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
  //        .body(new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR));
  //  }
}
