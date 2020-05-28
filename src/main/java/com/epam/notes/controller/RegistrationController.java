package com.epam.notes.controller;

import com.epam.notes.entity.Person;
import com.epam.notes.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class RegistrationController {
    @Autowired
    private PersonService personService;
    @Autowired
    private BCryptPasswordEncoder encoder;

    @PostMapping("/registration")
    public void registration(@RequestBody Person person) {
        if (personService.getPersonByName(person.getName()) == null) {
            person.setPassword(encoder.encode(person.getPassword()));
            personService.saveUser(person);
        } else throw new ResponseStatusException(
                HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS, "User already exists");
    }
}
