package com.epam.notes.controller;

import com.epam.notes.entity.Note;
import com.epam.notes.service.NoteService;
import com.epam.notes.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
public class NoteController {
    @Autowired
    private NoteService noteService;
    @Autowired
    private PersonService personService;

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
