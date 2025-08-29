package com.taekang.userservletapi.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 커스텀 예외에서 전달할 ErrorCode 리스트입니다. code의 prefix는 아래와 같이 정의합니다. C = Controller, S = Service 단에서, D =
 * Database에서 발생된 에러 C의 경우는 보통 rest API 소통 문제, S의 경우 database 에서 가져온 데이터의 가공 처리 중 발생한 문제 D의 경우는 쓰레드
 * 풀에서 요청한 데이터가 없을 경우로 정의된다고 판단하시면 됩니다. Code 의 숫자 나열은 HTTP 의 ERROR CODE에서 차용합니다. Code 만 받아도 이걸 본
 * 개발자는 바로 어떤 문제인지 식별되어야합니다.
 */
@Getter
public enum ErrorCode {
  /* GLOBAL ERROR : 특정할 수 없는 에러 */
  CANNOT_PROCESSING("C400", "처리할 수 없는 요청입니다.", HttpStatus.BAD_REQUEST),
  INTERNAL_SERVER_ERROR("S500", "서버 에러가 발생하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

  /* VALIDATION ERROR */
  VALIDATION_FAILED("C400", "DTO 검증에 실패했습니다.", HttpStatus.BAD_REQUEST),

  /* BOARD ERROR */
  NOTIFY_NOT_FOUNDED("S404", "공지를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

  /* AUTH ERROR */
  TOKEN_EXPIRE("C401", "이미 만료된 토큰입니다.", HttpStatus.UNAUTHORIZED),
  TOKEN_NOT_VALIDATE("C401", "인증되지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED),
  CANNOT_FIND_TOKEN("C401", "토큰을 찾을 수 없습니다.", HttpStatus.UNAUTHORIZED),
  IS_NOT_REFRESH_TOKEN("C400", "리프레시 토큰이 아닙니다.", HttpStatus.BAD_REQUEST),
  IS_NOT_ACCESS_TOKEN("C400", "엑세스 토큰이 아닙니다.", HttpStatus.BAD_REQUEST),

  /* FINANCIAL ERROR OF TETHER */
  IS_NOT_SUPPORT_WALLET_TYPE("S400", "지원하지 않는 지갑 형식입니다.", HttpStatus.BAD_REQUEST),
  CANNOT_FOUNDED_EXCHANGE_INFO("S500", "환율정보 수령을 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
  ACCOUNT_NOT_FOUND("D404", "지갑정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  DUPLICATE_ACCOUNT("D409", "중복된 계좌입니다.", HttpStatus.CONFLICT),
  INVALID_AMOUNT("C400", "잘못된 금액입니다.", HttpStatus.BAD_REQUEST),
  DEPOSIT_VERIFICATION("C400", "계정과 금액이 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
  WALLET_VERIFICATION("C400", "계정이나 지갑주소가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
  DEPOSIT_NOT_FOUND_OR_ALREADY_APPROVED(
      "C400", "입금기록을 찾을 수 없거나, 이미 승인되었습니다.", HttpStatus.BAD_REQUEST),
  DUPLICATE_USERNAME_WITH_DIFFERENT_WALLET("S400", "잘못된 유저이름입니다.", HttpStatus.BAD_REQUEST),
  DEPOSIT_NOT_FOUND("D404", "입금 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  FAILED_MASSAGE_SEND("S500", "이메일 전송을 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
  NOT_EQUALS_DEPOSIT_AMOUNT("C500", "실제 보낸 금액과 입금 금액이 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
  TRANSFER_VALIDATION_FAIL("S500", "거래 내역 검증에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
  CANNOT_FOUND_TRANSFER("S500", "거래 내역이 없습니다.", HttpStatus.BAD_REQUEST),

  /* Site */
  CANNOT_FOUND_SITE("S404", "사이트를 찾을 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

  /* USER ERROR CODE */
  INVALID_EMAIL("C400", "잘못된 이메일입니다.", HttpStatus.BAD_REQUEST),
  CANNOT_FOUND_USER("D404", "유저 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  ALREADY_EXIST_USERNAME("D400", "이미 존재하는 유저이름입니다.", HttpStatus.BAD_REQUEST),
  PASSWORD_INCORRECT("C401", "잘못된 패스워드입니다.", HttpStatus.UNAUTHORIZED),
  ALREADY_EXIST_EMAIL("D400", "이미 존재하는 이메일입니다.", HttpStatus.BAD_REQUEST);

  private final String code;
  private final String message;
  private final HttpStatus httpStatus;

  ErrorCode(String code, String message, HttpStatus httpStatus) {
    this.code = code;
    this.message = message;
    this.httpStatus = httpStatus;
  }

  @Override
  public String toString() {
    return "ErrorCode{"
        + "code='"
        + code
        + '\''
        + ", message='"
        + message
        + '\''
        + ", httpStatus="
        + httpStatus
        + '}';
  }
}
