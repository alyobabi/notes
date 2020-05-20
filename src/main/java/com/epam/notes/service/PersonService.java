package com.epam.notes.service;

import com.epam.notes.entity.Person;
import com.epam.notes.entity.User;
import com.epam.notes.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class PersonService {
    @Autowired
    private PersonRepository personRepository;

    public void saveUser(Person person) {
//        person.setPassword(encoder.encode(person.getPassword()));
        personRepository.save(person);
    }

    public Person getPersonByName(String name) {
        return personRepository.getPersonByName(name);
    }

    public Person getPersonByAuth(Authentication authentication) {
        if (authentication == null) return null;
        User user = (User) authentication.getPrincipal();
        return personRepository.getOne(user.getId());
    }

}
