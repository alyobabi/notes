package com.epam.notes.controller;

import com.epam.notes.entity.Person;
import com.epam.notes.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class RegistrationController {
    @Autowired
    private PersonService personService;
    @Autowired
    private BCryptPasswordEncoder encoder;

    @PostMapping("/registration")
    public void registration(@RequestBody Person person,
                             HttpServletRequest request,
                             HttpServletResponse response) throws Exception {
        if (personService.getPersonByName(person.getName()) == null) {
            person.setPassword(encoder.encode(person.getPassword()));
            personService.saveUser(person);
        } else throw new Exception("User with this name already exists");
    }
}
