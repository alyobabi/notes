package com.epam.notes;

import com.epam.notes.entity.Person;
import com.epam.notes.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class NotesApplicationTests {
	@Autowired
	UserRepository userRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void saveUser() {
		Person person = new Person();
		person.setName("eleph");
		person.setPassword("ddd");
		userRepository.save(person);
	}

}
