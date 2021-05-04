package org.skb.registration.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.skb.registration.dao.entity.Registration;
import org.skb.registration.dao.repositrory.RegistrationRepository;
import org.skb.registration.messaging.producer.RegistrationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {
    Logger log = LoggerFactory.getLogger(RegistrationService.class);

    private final RegistrationRepository registrationRepository;
    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.producer}")
    private String producer;

    @Autowired
    public RegistrationService(
            RegistrationRepository registrationRepository,
            RabbitTemplate rabbitTemplate
    ) {
        this.registrationRepository = registrationRepository;
        this.rabbitTemplate = rabbitTemplate;
    }


    public Long createUser(String login, String password, String email, String fullname) {
        Registration registration = new Registration(
                login, new BCryptPasswordEncoder().encode(password), email, fullname
        );
        registration = registrationRepository.save(registration);

        RegistrationMessage registrationMessage = new RegistrationMessage();
        registrationMessage.setLogin(registration.getLogin());
        registrationMessage.setEmail(registration.getEmail());
        registrationMessage.setFullname(registration.getFullname());

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            rabbitTemplate.convertAndSend(producer, producer,
                    objectMapper.writeValueAsString(registrationMessage));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }

        return registration.getUserId();
    }
}
