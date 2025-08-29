package com.taekang.userservletapi.rabbitMQ;

import com.taekang.userservletapi.DTO.crypto.DepositSentApprovalNotifyDTO;
import com.taekang.userservletapi.error.FailedMessageSendException;
import com.taekang.userservletapi.service.MailSenderService;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageConsumer {

  private final MailSenderService mailSenderService;
  private final JavaMailSender javaMailSender;

  @Autowired
  public MessageConsumer(MailSenderService mailSenderService, JavaMailSender javaMailSender) {
    this.mailSenderService = mailSenderService;
    this.javaMailSender = javaMailSender;
  }

  @RabbitListener(queues = "${rabbitmq.deposit.approval.queue}")
  public void receiveDepositMessage(DepositSentApprovalNotifyDTO message, @Header("site") String siteCode) {
    log.info("Received deposit message: {}", message.toString());

    MimeMessage mail = mailSenderService.createDepositApprovalMail(message.getEmail(), message);

    try {

    javaMailSender.send(mail);
    } catch (Exception e) {
      throw new FailedMessageSendException();
    }
  }
}
