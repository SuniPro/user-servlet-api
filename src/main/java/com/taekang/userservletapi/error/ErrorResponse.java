package com.taekang.userservletapi.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

  private String detailMessage;
  private String code;
  private String message;
  private int httpStatus;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private final LocalDateTime timestamp = LocalDateTime.now();

  public ErrorResponse(ErrorCode errorCode) {
    this.code = errorCode.getCode();
    this.message = errorCode.getMessage();
    this.httpStatus = errorCode.getHttpStatus().value();
  }

  public ErrorResponse(ErrorCode errorCode, String detailMessage) {
    this.httpStatus  = errorCode.getHttpStatus().value();
    this.code    = errorCode.getCode();
    this.message = errorCode.getMessage();
    this.detailMessage = detailMessage;
  }
}
