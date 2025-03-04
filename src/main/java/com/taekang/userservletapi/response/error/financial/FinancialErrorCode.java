package com.taekang.userservletapi.response.error.financial;

import lombok.Getter;

/** 금융 거래 중 발생할 수 있는 오류입니다. * */
@Getter
public enum FinancialErrorCode {
  USER_NOT_FOUND(400, "USER_NOT_FOUND", "유저를 찾을 수 없습니다."),

  LACK_OF_MONEY(402, "LACK_OF_MONEY", "머니가 부족합니다."),

  YOU_ARE_LOCKED(400, "YOU_ARE_LOCKED", "권한이 부족하여, 해당 작업을 수행할 수 없습니다."),

  TOO_MANY_REQUESTS(429, "TOO_MANY_REQUESTS", "<p>요청 수가 초과되었습니다. <br> 새로고침 후 다시 시도해주세요.</p>"),

  UNAVAILABLE_FOR_LEGAL_REASONS(451, "UNAVAILABLE_FOR_LEGAL_REASONS", "해당 계좌는 법적인 이유로 정지된 계좌입니다.");

  private final int status;
  private final String code;
  private final String description;

  FinancialErrorCode(int status, String code, String description) {
    this.status = status;
    this.code = code;
    this.description = description;
  }
}
