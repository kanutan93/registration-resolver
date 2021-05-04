package org.skb.registration.controller;

import org.skb.registration.dto.RegistrationRequestDTO;
import org.skb.registration.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private RegistrationService registrationService;

    @Autowired
    public UserController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/")
    public Long createUser(@RequestBody RegistrationRequestDTO user) {
       return this.registrationService.createUser(
                user.getLogin(), user.getPassword(), user.getEmail(), user.getFullname()
        );
    }
}

