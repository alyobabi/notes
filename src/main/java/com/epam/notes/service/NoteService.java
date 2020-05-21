package com.epam.notes.service;

import com.epam.notes.entity.Note;
import com.epam.notes.repository.NoteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NoteService {
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private PersonService personService;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public void createNote(Note note, Authentication authentication) {
        note.setPerson(personService.getPersonByAuth(authentication));
        note.setDateCreated(LocalDateTime.now());
        note.setDateEdited(LocalDateTime.now());
        note.setDeleted(false);
        noteRepository.save(note);
    }

    public void saveNote(Note note, Authentication authentication) {
        try {
            if (!personService.getPersonByAuth(authentication).getId().equals(note.getPerson().getId())) {
                note.setDateEdited(LocalDateTime.now());
                noteRepository.save(note);
            } else throw new Exception("It is not your note");
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public void deleteNote(Long id, Authentication authentication) {
        Note note = noteRepository.getOne(id);
        try {
            if (!personService.getPersonByAuth(authentication).getId().equals(note.getPerson().getId())) {
                note.setDeleted(true);
                noteRepository.save(note);
            } else throw new Exception("It is not your note");
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public List<Note> getNotes(Authentication authentication) {
        noteRepository.findAllByPerson(personService.getPersonByAuth(authentication));
        return noteRepository.findAll();
    }

    public void exportNote(Long noteId,
                           HttpServletResponse response,
                           Authentication authentication) throws Exception {
        Optional<Note> noteOptional = getNotes(authentication)
                .stream()
                .filter(n -> n.getId()
                        .equals(noteId)).findFirst();
        if (noteOptional.isPresent()) {
            Note note = noteOptional.get();
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(note);
            String path = String.format("%s.json", note.getTitle());
            FileWriter fileWriter = new FileWriter(path);
            fileWriter.write(jsonString);
            fileWriter.flush();
            download(response, path);
        } else throw new Exception("Import note failed");
    }

    //todo refactor
    private void download(
            HttpServletResponse response, String path
    ) throws IOException {
        File file = new File(path);
        response.setContentType("application/json");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setIntHeader("Expires", 0);
        response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" +
                path);
        response.setContentLengthLong(file.length());

        try (BufferedInputStream fis = new BufferedInputStream(
                new FileInputStream(file))) {
            int i;
            byte[] buffer = new byte[1024];
            ServletOutputStream out = response.getOutputStream();
            while ((i = fis.read(buffer)) > 0) out.write(buffer, 0, i);
            out.flush();
        }
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
}
