package com.epam.notes.repository;

import com.epam.notes.entity.Note;
import com.epam.notes.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long>{
    public List<Note> findAllByPerson(Person person);
}
