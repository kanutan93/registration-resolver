package org.skb.resolver.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skb.resolver.messaging.consumer.RegistrationMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class ResolverServiceTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private ResolverService resolverService;

    @Test
    public void resolverTest() {
        RegistrationMessage registrationMessage = new RegistrationMessage();
        registrationMessage.setLogin("test");
        registrationMessage.setEmail("test@gmail.com");
        registrationMessage.setFullname("testFullname");

        doNothing().when(rabbitTemplate).convertAndSend(anyString(), anyString(), anyString());

        resolverService.resolve(registrationMessage);

        verify(rabbitTemplate, times(1)).convertAndSend(anyString(), anyString(), anyString());
    }
}
