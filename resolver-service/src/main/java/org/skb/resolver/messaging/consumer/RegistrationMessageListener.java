package org.skb.resolver.messaging.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.skb.resolver.service.ResolverService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationMessageListener {

    private ResolverService resolverService;

    @Autowired
    public RegistrationMessageListener(ResolverService resolverService) {
        this.resolverService = resolverService;
    }

    @RabbitListener(queues = "${spring.rabbitmq.consumer}")
    public void receive(Message message) throws Exception {
        String body = new String(message.getBody());
        RegistrationMessage registrationMessage = new ObjectMapper().readValue(body, RegistrationMessage.class);
        resolverService.resolve(registrationMessage);
    }
}
