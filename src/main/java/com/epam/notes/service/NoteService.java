package com.epam.notes.service;

import com.epam.notes.entity.Note;
import com.epam.notes.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NoteService {
    @Autowired
    private NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public void createNote(Note note) {
        note.setDateCreated(LocalDateTime.now());
        note.setDateEdited(LocalDateTime.now());
        note.setDeleted(false);
        noteRepository.save(note);
    }

    public void editNote(Note note) {
        note.setDateEdited(LocalDateTime.now());
        noteRepository.save(note);
    }

    public void deleteNote(Long id) {
        Note note = noteRepository.getOne(id);
        note.setDeleted(true);
        noteRepository.save(note);
    }

    public List<Note> getNotes() {
        return noteRepository.findAll();
    }

    public Note getNoteById(Long id) {
        return noteRepository.getOne(id);
    }

    public void exportNote(String jsonString) throws Exception {
        Note note = new Note();
        note.setTitle(parsing("title", jsonString));
        note.setText(parsing("text", jsonString));
        createNote(note);
    }

    private String parsing(String name, String jsonString) throws Exception {
        int ind = jsonString.indexOf(name);
        if (ind < 0) throw new Exception("exporting JSON has not correct format");
        StringBuilder temp = new StringBuilder(jsonString);
        int start = temp.indexOf(":", ind + name.length()) + 1;
        int end = temp.indexOf(",", start);
        String res = temp.substring(start, end).trim();
        if (res.charAt(0) == '"' && res.charAt(res.length() - 1) == '"')
            return res.substring(1, res.length() - 1);
        return res;
    }

    public InputStreamResource downloadFile(Long id) throws IOException {
        Note note = noteRepository.getOne(id);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(note);
        oos.flush();
        oos.close();
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        try {
            InputStreamResource resource = new InputStreamResource(is);
            if (resource.exists()) return resource;
            else throw new Exception("File not found");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




}
