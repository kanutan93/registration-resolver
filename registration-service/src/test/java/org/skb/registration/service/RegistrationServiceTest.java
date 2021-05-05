package org.skb.registration.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skb.registration.dao.entity.Registration;
import org.skb.registration.dao.repositrory.RegistrationRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceTest {

    @Mock
    private RegistrationRepository registrationRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private RegistrationService registrationService;

    @Test
    public void registerTest() {
        Registration registration = new Registration();
        registration.setUserId(1L);
        registration.setLogin("test");
        registration.setPassword("123");
        registration.setEmail("test@gmail.com");
        registration.setFullname("testFullname");

        doNothing().when(rabbitTemplate).convertAndSend(anyString(), anyString(), anyString());
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        long id = registrationService.register(
                registration.getLogin(), registration.getPassword(),
                registration.getEmail(), registration.getFullname()
        );

        Assertions.assertEquals(1, id);
        verify(rabbitTemplate, times(1)).convertAndSend(anyString(), anyString(), anyString());
        verify(registrationRepository, times(1)).save(any(Registration.class));
    }

}
