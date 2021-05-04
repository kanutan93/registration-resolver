package org.skb.notification.messaging.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.skb.notification.service.NotificationService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResolverMessageListener {

    private NotificationService notificationService;

    @Autowired
    public ResolverMessageListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RabbitListener(queues = "${spring.rabbitmq.consumer}")
    public void receive(Message message) throws Exception {
        String body = new String(message.getBody());
        ResolverMessage resolverMessage = new ObjectMapper().readValue(body, ResolverMessage.class);
        notificationService.sendMail(resolverMessage.getToAddress(), resolverMessage.getMessageBody());
    }
}
