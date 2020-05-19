package com.epam.notes.controller;

import com.epam.notes.entity.Note;
import com.epam.notes.repository.NoteRepository;
import com.epam.notes.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/notes")
public class NoteController {
    @Autowired
    private NoteService noteService;
    @Autowired
    private NoteRepository noteRepository;

    @GetMapping("/getNotes")
    public List<Note> getNotes() {
        return noteService.getNotes();
    }

    @PostMapping("/createNote")
    public void createNote(@RequestBody Note note) {
        noteService.createNote(note);
    }

    @PostMapping("/editNote")
    public void editNote(@RequestBody Note note) {
        noteService.editNote(note);
    }

    @DeleteMapping("/deleteNote/{id}")
    public void deleteNote(@PathVariable Long id) {
        noteService.deleteNote(id);
    }

//    @PostMapping("/exportNote")
//    public void exportNote(@RequestBody Note note) throws Exception {
//        noteService.exportNote(json);
//    }

    //@GetMapping("/importNote")

//@GetMapping("/downloadFile/{id}")
//    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable Long id, HttpServletRequest request) throws Exception {
//        Note note = noteRepository.getOne(id);
//        byte[] content = IOUtils.toByteArray(object.getObjectContent());
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        ObjectOutputStream oos = new ObjectOutputStream(baos);
//        oos.writeObject(note);
//        oos.flush();
//        oos.close();
//        InputStream is = new ByteArrayInputStream(baos.toByteArray());
//        InputStreamResource resource = new InputStreamResource(is);
//        HttpHeaders respHeaders = new HttpHeaders();
//        respHeaders.setContentType(MediaType.valueOf("application/pdf"));
//        respHeaders.setContentLength(resource.contentLength());
//        respHeaders.setContentDispositionFormData("attachment", "fileNameIwant.pdf");
//
//        return new ResponseEntity<>(resource, respHeaders, HttpStatus.OK);
//    }



}
