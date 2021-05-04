package org.skb.resolver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.skb.resolver.messaging.consumer.RegistrationMessage;
import org.skb.resolver.messaging.producer.ResolverMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ResolverService {

    private static Logger log = LoggerFactory.getLogger(ResolverService.class);

    @Value("${spring.rabbitmq.producer}")
    private String producer;

    private RabbitTemplate rabbitTemplate;

    @Autowired
    public ResolverService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void resolve(RegistrationMessage registrationMessage) {
        boolean isRejected = shouldReject();
        String login = registrationMessage.getLogin();
        String fullname = registrationMessage.getFullname();
        log.info("Registration solved with result {} for {} ({}).", !isRejected, login, fullname);

        ResolverMessage resolverMessage = new ResolverMessage();
        resolverMessage.setToAddress(registrationMessage.getEmail());

        String messageBody = String.format(
                "Hello %s (%s). " +  (isRejected ? " Your request was rejected" : "Your request was approved"),
                login, fullname
        );
        resolverMessage.setMessageBody(messageBody);

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            rabbitTemplate.convertAndSend(producer, producer,
                    objectMapper.writeValueAsString(resolverMessage));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }

    }

    private boolean shouldReject() {
        return new Random().nextInt(10) == 1;
    }
}
