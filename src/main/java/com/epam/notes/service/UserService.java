package com.epam.notes.service;

//import com.epam.notes.entity.Note;
import com.epam.notes.entity.Person;

public interface UserService {

    public void saveUser(Person person);

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
