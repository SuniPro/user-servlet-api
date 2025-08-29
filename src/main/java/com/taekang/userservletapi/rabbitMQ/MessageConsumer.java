package com.taekang.userservletapi.rabbitMQ;

import com.taekang.userservletapi.DTO.crypto.CryptoDepositDTO;
import com.taekang.userservletapi.service.MailSenderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
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
    public void receiveDepositMessage(CryptoDepositDTO message) {
        log.info("Received deposit message: {}", message.toString());

//        MimeMessage mail = mailSenderService.createDepositApprovalMail(message.getEmail(), message);
//
//        javaMailSender.send(mail);
        // 여기서부터 받은 메시지(DTO)를 가지고 비즈니스 로직을 수행하면 됩니다.
        // 예: employee-server에서 입금 승인 처리, 알림 발송 등
    }
}
