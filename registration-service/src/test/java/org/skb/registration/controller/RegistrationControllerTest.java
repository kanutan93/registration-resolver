package org.skb.registration.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skb.registration.RegistrationApplication;
import org.skb.registration.config.H2TestConfig;
import org.skb.registration.dao.entity.Registration;
import org.skb.registration.dao.repositrory.RegistrationRepository;
import org.skb.registration.service.RegistrationService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {RegistrationApplication.class, H2TestConfig.class})
@AutoConfigureMockMvc
public class RegistrationControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private RegistrationService registrationService;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }


    @Test
    public void createRegistration() throws Exception {
        doNothing().when(rabbitTemplate).convertAndSend(anyString(), anyString(), anyString());

        String jsonString = new JSONObject()
                .put("login", "test")
                .put("password", "123")
                .put("email", "test@gmail.com")
                .put("fullname", "testFullname")
                .toString();
        MvcResult mvcResult = createRegistrationMvcRequest(jsonString);
        String result = mvcResult.getResponse().getContentAsString();

        Assertions.assertEquals(1L, Long.parseLong(result));
        Assertions.assertEquals("test", registrationRepository.findById(1L).get().getLogin());
        verify(rabbitTemplate, times(1)).convertAndSend(anyString(), anyString(), anyString());

    }

    private MvcResult createRegistrationMvcRequest(String content) throws Exception {
        return mockMvc.perform(post("/")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

}
