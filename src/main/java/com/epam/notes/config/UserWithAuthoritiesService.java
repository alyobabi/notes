package com.epam.notes.config;

import com.epam.notes.entity.Person;
import com.epam.notes.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;


public class UserWithAuthoritiesService implements UserDetailsService {

    private final PersonRepository personRepository;

    public UserWithAuthoritiesService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    //@Transactional(readOnly = true)
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
