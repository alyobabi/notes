package com.epam.notes.service;

import com.epam.notes.NoRightsException;
import com.epam.notes.entity.*;
import com.epam.notes.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PersonService {
    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public void saveUser(Person person) {
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

    private List<Note> getNotesFromOthers(Authentication authentication, Rights rights) {
        try {
            if (authentication == null) throw new NoRightsException();
        } catch (NoRightsException e) {
            e.getStackTrace();
        }
        Person person = getPersonByAuth(authentication);
        Set<NoteSharing> noteSharing = person.getNoteSharing();
        if (noteSharing == null) return null;
        return noteSharing.stream()
                .filter(n -> n.getRights()
                        .equals(rights))
                .map(NoteSharing::getNote)
                .collect(Collectors.toList());
    }

    public List<Note> getAlienNotesToRead(Authentication authentication) {
        return getNotesFromOthers(authentication, Rights.READ);
    }

    public List<Note> getAlienNotesToWrite(Authentication authentication) {
        return getNotesFromOthers(authentication, Rights.WRITE);
    }
}
