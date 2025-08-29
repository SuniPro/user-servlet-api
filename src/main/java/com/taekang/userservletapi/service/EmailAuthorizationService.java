package com.taekang.userservletapi.service;

import com.taekang.userservletapi.error.FailedMessageSendException;
import com.taekang.userservletapi.error.InvalidEmailException;
import jakarta.mail.internet.MimeMessage;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailAuthorizationService {

  private final JavaMailSender javaMailSender;
  private final NeverBounceService neverBounceService;

  private final MailSenderService mailSenderService;

  private static final Pattern EMAIL_PATTERN =
      Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

  @Autowired
  public EmailAuthorizationService(
      JavaMailSender javaMailSender,
      NeverBounceService neverBounceService,
      MailSenderService mailSenderService) {
    this.javaMailSender = javaMailSender;
    this.neverBounceService = neverBounceService;
    this.mailSenderService = mailSenderService;
  }

  public void sendAuthMail(String to) throws FailedMessageSendException {
    if (!isValidEmailFormat(to)) {
      throw new InvalidEmailException();
    }

    if (!neverBounceService.isEmailValid(to)) {
      throw new InvalidEmailException();
    }

    try {
      MimeMessage mail = mailSenderService.createAuthMail(to);
      javaMailSender.send(mail);
      log.info("{} 주소로 발송됨", to);
    } catch (MailSendException e) {
      throw new FailedMessageSendException();
    }
  }

  private boolean isValidEmailFormat(String email) {
    return EMAIL_PATTERN.matcher(email).matches();
  }
}
