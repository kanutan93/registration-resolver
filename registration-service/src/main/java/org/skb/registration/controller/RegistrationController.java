package org.skb.registration.controller;

import org.hibernate.PropertyValueException;
import org.skb.registration.dto.RegistrationRequestDTO;
import org.skb.registration.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

    private RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/")
    public ResponseEntity<Long> createRegistration(@RequestBody RegistrationRequestDTO user) {
        try {
            Long id = this.registrationService.register(
                    user.getLogin(), user.getPassword(), user.getEmail(), user.getFullname()
            );
            return ResponseEntity.ok(id);
        } catch (Exception e) {
            if (e instanceof DataIntegrityViolationException || e instanceof PropertyValueException) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

