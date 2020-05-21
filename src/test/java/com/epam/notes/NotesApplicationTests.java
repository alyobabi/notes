package com.epam.notes;

import com.epam.notes.entity.Person;
import com.epam.notes.repository.PersonRepository;
import com.epam.notes.service.NoteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileWriter;
import java.io.IOException;

@SpringBootTest
class NotesApplicationTests {
	@Autowired
    PersonRepository personRepository;
	@Autowired
	NoteService noteService;

	@Test
	void contextLoads() {
	}

	@Test
	void saveUser() {
		Person person = new Person();
		person.setName("eleph");
		person.setPassword("ddd");
		personRepository.save(person);
	}

}
