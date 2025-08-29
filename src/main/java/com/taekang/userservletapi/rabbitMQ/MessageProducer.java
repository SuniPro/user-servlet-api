package com.taekang.userservletapi.rabbitMQ;

import com.taekang.userservletapi.DTO.crypto.DepositNotifyDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageProducer {

    @Value("${rabbitmq.transaction.exchange}")
    private String transactionExchangeName;

    @Value("${rabbitmq.deposit.request.routing}")
    private String depositRequestRoutingKey;

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public MessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendDepositMessage(DepositNotifyDTO message, String siteCode) {
        Message<DepositNotifyDTO> msg = MessageBuilder.withPayload(message)
                .setHeader("site", siteCode)
                .build();
        rabbitTemplate.convertAndSend(transactionExchangeName, depositRequestRoutingKey, msg);
    }
}
