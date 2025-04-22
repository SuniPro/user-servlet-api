package com.taekang.userservletapi.service;

import com.taekang.userservletapi.error.FailedMessageSendException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailAuthorizationService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Autowired
    public EmailAuthorizationService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public String generateAuthCode() {
        return String.valueOf((int) ((Math.random() * 900000) + 100000)); // 6자리 인증번호
    }

    public void sendAuthMail(String to) throws FailedMessageSendException {
        MimeMessage mail = createMail(to);
        javaMailSender.send(mail);

        log.info("{} 주소로 발송됨", to);
    }

    public MimeMessage createMail(String mail) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        log.info("{} 을 통해 메일을 발송함.", senderEmail);

        try {
            mimeMessage.setFrom(senderEmail);
            mimeMessage.setRecipients(MimeMessage.RecipientType.TO, mail);
            mimeMessage.setSubject("i coins 이메일 인증 안내");
            String body = "<!DOCTYPE html>" +
                          "<html>" +
                          "<head>" +
                          "  <meta charset=\"UTF-8\">" +
                          "  <title>이메일 인증</title>" +
                          "</head>" +
                          "<body style=\"font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;\">" +
                          "  <div style=\"max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 30px; border-radius: 10px; box-shadow: 0 2px 5px rgba(0,0,0,0.1);\">" +
                          "    <h2 style=\"color: #333333;\"><span style=\"color: #037AED;\">i coins</span> 이메일 인증 안내</h2>" +
                          "    <p style=\"font-size: 16px; color: #555555;\">안녕하세요. i coins를 이용해주셔서 감사합니다.<br><br>" +
                          "    이 메일은 인증 확인용입니다.</p>" +
                          "  </div>" +
                          "</body>" +
                          "</html>";
            mimeMessage.setContent(body, "text/html; charset=UTF-8");

            return mimeMessage;
        } catch (MessagingException e) {
            log.error(e.getMessage());
            throw new FailedMessageSendException();
        }
    }
}
