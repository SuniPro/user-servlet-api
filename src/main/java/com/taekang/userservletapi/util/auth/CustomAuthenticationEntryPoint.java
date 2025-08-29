package com.taekang.userservletapi.util.auth;

import com.taekang.userservletapi.error.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException {

    Object exceptionAttr = request.getAttribute("exception");
    ErrorCode errorCode;

    if (exceptionAttr == null) {
      errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
    } else {
      String exception = exceptionAttr.toString();
      log.debug("exception: {}", exception);

      switch (exception) {
        case "SignatureVerificationException",
            "AlgorithmMismatchException",
            "JWTDecodeException" -> errorCode = ErrorCode.TOKEN_NOT_VALIDATE;
        case "TokenExpiredException" -> errorCode = ErrorCode.TOKEN_EXPIRE;
        default -> errorCode = ErrorCode.CANNOT_FIND_TOKEN;
      }
    }
    setResponse(response, errorCode);
  }

  private void setResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
    response.setContentType("application/json;charset=UTF-8");
    response.setStatus(errorCode.getHttpStatus().value());
    response.getWriter().println(errorCode);
  }
}
