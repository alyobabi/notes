package com.epam.notes.service;

import com.epam.notes.entity.Person;
import com.epam.notes.entity.User;
import com.epam.notes.repository.PersonRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserService implements UserDetailsService {

    private final PersonRepository personRepository;

    public UserService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public User loadUserByUsername(String name) throws UsernameNotFoundException {
        Person person = personRepository.getPersonByName(name);
        if (name != null) {
            User user = new User();
            user.setId(person.getId());
            user.setName(person.getName());
            user.setPassword(person.getPassword());
            return user;
        } else throw new UsernameNotFoundException(null);
    }
}
