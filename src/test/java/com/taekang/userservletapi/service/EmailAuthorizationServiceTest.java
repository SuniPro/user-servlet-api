package com.taekang.userservletapi.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class EmailAuthorizationServiceTest {

    @Autowired
    private EmailAuthorizationService emailAuthorizationService;

    @Test
    void testSendAuthMail_success() {
        String email = "mommom22558800@gmail.com";

        emailAuthorizationService.sendAuthMail(email);
    }
//
//    @Test
//    void testSendAuthMail_fail_send() {
//        String email = "fail@example.com";
//        doThrow(new MailSendException("메일 전송 실패")).when(javaMailSender).send(any(MimeMessage.class));
//
//        assertThrows(FailedMessageSendException.class, () -> emailAuthorizationService.sendAuthMail(email));
//    }
}

