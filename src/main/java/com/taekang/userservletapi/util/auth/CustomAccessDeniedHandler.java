package com.taekang.userservletapi.util.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Setter
@Component
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  private String errorPage;

  @Override
  public void handle(
      HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse,
      AccessDeniedException e)
      throws IOException {

    String deniedUrl = errorPage + "?exception=" + e.getMessage();
    httpServletResponse.sendRedirect(deniedUrl);
  }
}
