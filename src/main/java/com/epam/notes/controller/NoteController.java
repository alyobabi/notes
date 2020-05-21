package com.epam.notes.controller;

import com.epam.notes.entity.Note;
import com.epam.notes.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/notes")
public class NoteController {
    @Autowired
    private NoteService noteService;

    @GetMapping("/getNotes")
    public List<Note> getNotes(Authentication authentication) {
        return noteService.getNotes(authentication);
    }

    @PostMapping("/createNote")
    public void createNote(@RequestBody Note note, Authentication authentication) {
        noteService.createNote(note, authentication);
    }

    @PostMapping("/saveNote")
    public void editNote(@RequestBody Note note, Authentication authentication) {
        noteService.saveNote(note, authentication);
    }

    @DeleteMapping("/deleteNote/{id}")
    public void deleteNote(@PathVariable Long id, Authentication authentication) {
        noteService.deleteNote(id, authentication);
    }

    @GetMapping("/exportNote/{id}")
    public void exportNote(@PathVariable Long id,
                           HttpServletResponse response,
                           Authentication authentication) throws Exception {
        noteService.exportNote(id, response, authentication);
    }

    //it is the same DELETE LATER
    @PostMapping("/importNote")
    public void importNote(@RequestBody Note note, Authentication authentication) {
        noteService.createNote(note, authentication);
    }




}
