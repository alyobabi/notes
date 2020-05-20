package com.epam.notes.service;

import com.epam.notes.entity.Person;
import com.epam.notes.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    BCryptPasswordEncoder encoder;
    @Autowired
    PersonRepository personRepository;

    public void saveUser(Person person) {
        person.setPassword(encoder.encode(person.getPassword()));
        personRepository.save(person);
    }

    public Person getPersonByName(String name) {
        return personRepository.getPersonByName(name);
    }

    /*@Autowired
    private PersonRepository personRepository;

    public User findUserById(Long id) {
        Optional<User> userFromDb = personRepository.findById(id);
        return userFromDb.orElse(new User());
    }

    public void createPerson(User user) {
        personRepository.save(user);
    }

    public List<User> getPeople() {
        return personRepository.findAll();
    }

    public Set<Note> getNotesByPerson(Long id) {
        User user = personRepository.getOne(id);
        return user.getNotes();
    }*/

}
