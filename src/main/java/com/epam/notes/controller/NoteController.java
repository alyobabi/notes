package com.epam.notes.controller;

import com.epam.notes.entity.Note;
import com.epam.notes.entity.NoteHistory;
import com.epam.notes.service.NoteService;
import com.epam.notes.service.PersonService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {
    @Autowired
    private NoteService noteService;
    @Autowired
    private PersonService personService;

    @GetMapping("/getNote/{id}")
    public Note getNote(@PathVariable Long id, Authentication authentication) {
        return noteService.getNote(id, authentication);
    }

    @GetMapping("/getNotes")
    public List<Note> getNotes(Authentication authentication) {
        return noteService.getNotes(authentication);
    }

    @PostMapping("/createNote")
    public Note createNote(@RequestBody Note note, Authentication authentication) {
        return noteService.createNote(note, authentication);
    }

    @PostMapping("/notSave")
    public Note notSave(@RequestBody Note note) {
        return noteService.notSaveNote(note);
    }

    @PostMapping("/saveNote")
    public Note saveNote(@RequestBody Note note, Authentication authentication) {
        return noteService.saveNote(note, authentication);
    }

    @DeleteMapping("/deleteNote/{id}")
    public void deleteNote(@PathVariable Long id, Authentication authentication) {
        noteService.deleteNote(id, authentication);
    }

    @GetMapping("/exportNote/{id}")
    public void exportNote(@PathVariable Long id,
                           HttpServletResponse response,
                           Authentication authentication) throws IOException {
        noteService.download(id, response, authentication);
    }

    @GetMapping("/getHistory/{id}")
    public List<NoteHistory> getHistory(@PathVariable Long id,
                                        Authentication authentication) throws IOException {
        return noteService.getHistory(id, authentication);
    }

    @PostMapping("/importNote")
    public void importNote(@RequestParam("file") MultipartFile file,
                           Authentication authentication) throws IOException {
        noteService.importNote(file, authentication);
    }

    @PostMapping("/shareNote")
    public void shareNote(@RequestParam String name,
                          @RequestParam Long id,
                          @RequestParam String rights,
                          Authentication authentication) {
        noteService.shareNote(name, id, rights, authentication);
    }

    @GetMapping("/getAlienNotesToRead")
    public List<Note> getAlienNotesToRead(Authentication authentication) {
        return personService.getAlienNotesToRead(authentication);
    }

    @GetMapping("/getAlienNotesToWrite")
    public List<Note> getAlienNotesToWrite(Authentication authentication) {
        return personService.getAlienNotesToWrite(authentication);
    }

}
