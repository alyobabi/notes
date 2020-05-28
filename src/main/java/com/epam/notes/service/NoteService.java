package com.epam.notes.service;

import com.epam.notes.entity.*;
import com.epam.notes.repository.NoteHistoryRepository;
import com.epam.notes.repository.NoteRepository;
import com.epam.notes.repository.NoteSharingRepository;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NoteService {
    private final NoteRepository noteRepository;
    private final PersonService personService;
    private final NoteSharingRepository noteSharingRepository;
    private final NoteHistoryRepository noteHistoryRepository;
    private final WebSocketService webSocketService;

    public NoteService(NoteRepository noteRepository, PersonService personService,
                       NoteSharingRepository noteSharingRepository, NoteHistoryRepository noteHistoryRepository, WebSocketService webSocketService) {
        this.noteRepository = noteRepository;
        this.personService = personService;
        this.noteSharingRepository = noteSharingRepository;
        this.noteHistoryRepository = noteHistoryRepository;
        this.webSocketService = webSocketService;
    }

    public Note createNote(Note note, Authentication authentication) {
        note.setPerson(personService.getPersonByAuth(authentication));
        note.setDateCreated(LocalDateTime.now());
        note.setDateEdited(LocalDateTime.now());
        note.setDeleted(false);
        return noteRepository.save(note);
    }

    public Note notSaveNote(Note note) {
        //add
        webSocketService.sendNewText(note.getText(), note.getId());
        return note;
    }

    public Note saveNote(Note noteFromClient, Authentication authentication) {
        Note note = noteRepository.getOne(noteFromClient.getId());
        if (!note.getText().isEmpty() ) {
            //todo delete this block ->
            // change db ->
            // refactor this method (how to saveEdit??)
            NoteHistory noteHistory = new NoteHistory();
            noteHistory.setIdNote(note.getId());
            noteHistory.setTitle(note.getTitle());
            noteHistory.setText(note.getText());
            noteHistory.setDate(note.getDateEdited());
            noteHistoryRepository.save(noteHistory);
        }
        checkAuthentication(authentication, note, true);
        note.setDateEdited(LocalDateTime.now());
        note.setTitle(noteFromClient.getTitle());
        note.setText(noteFromClient.getText());
        noteRepository.save(note);
//        webSocketService.sendNewText(note.getText(), note.getId());
        return note;
    }

    public void deleteNote(Long id, Authentication authentication) {
        Note note = noteRepository.getOne(id);
        checkAuthentication(authentication, note, true);
        note.setDeleted(true);
        noteRepository.save(note);
    }

    public List<Note> getNotes(Authentication authentication) {
        return noteRepository.findAllByPerson(personService.getPersonByAuth(authentication));
    }

    public Note getNote(Long id, Authentication authentication) {
        Note note = noteRepository.getOne(id);
        checkAuthentication(authentication, note, true);
        return note;
    }

    public List<NoteHistory> getHistory(Long id, Authentication authentication) {
        checkAuthentication(authentication, noteRepository.getOne(id), false);
        return noteHistoryRepository.findAllByIdNote(id);
    }

    //todo refactor end of text
    public void importNote(MultipartFile file, Authentication authentication) {
        try {
            Note note = new Note();
            String content = new String(file.getBytes());
            String beginText = "text";
//        note.setTitle(content.substring(content.indexOf(beginTitle) + beginTitle.length() + 3, content.indexOf("\",\"")));
            String title = file.getOriginalFilename();
            if (title != null && title.contains(".json")) title = title.replace(".json", "");
            note.setTitle(title);
            note.setPerson(personService.getPersonByAuth(authentication));
            note.setDateCreated(LocalDateTime.now());
            note.setDateEdited(LocalDateTime.now());
            note.setDeleted(false);
            note.setText(content.substring(content.indexOf(beginText) + beginText.length() + 3, content.indexOf("\"}")));
            noteRepository.save(note);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Can not do import", e);
        }
    }

    public void download(Long id,
                         HttpServletResponse response,
                         Authentication authentication) throws IOException {
        Note note = noteRepository.getOne(id);
        checkAuthentication(authentication, note, false);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonString = objectMapper.writeValueAsString(note);
        String path = String.format("%s.json", note.getTitle());
        response.setContentType("application/json");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setIntHeader("Expires", 0);
        response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" +
                path);
        InputStream inputStream = IOUtils.toInputStream(jsonString);
        ServletOutputStream outputStream = response.getOutputStream();
        int i;
        byte[] buffer = new byte[1024];
        while ((i = inputStream.read(buffer)) > 0) outputStream.write(buffer, 0, i);
        outputStream.flush();
        outputStream.close();
    }

    public void shareNote(String namePerson, Long idNote, String rights, Authentication authentication) {
        Note note = noteRepository.getOne(idNote);
        Person person = personService.getPersonByName(namePerson);
        checkAuthentication(authentication, note, false);
        NoteSharing noteSharing = new NoteSharing();
        noteSharing.setPerson(person);
        noteSharing.setNote(note);
        if (rights.contains("read")) noteSharing.setRights(Rights.READ);
        else if (rights.contains("write")) noteSharing.setRights(Rights.WRITE);
        noteSharingRepository.save(noteSharing);

    }

    private void checkAuthentication(Authentication authentication, Note note, boolean writeOthers) {
        try {
            if (!(personService.getPersonByAuth(authentication).getId().equals(note.getPerson().getId()) ||
                    (writeOthers && note.getNoteSharing()
                            .stream()
                            .filter(noteSharing -> noteSharing.getRights().equals(Rights.WRITE))
                            .map(NoteSharing::getPerson)
                            .anyMatch(person -> person.getId()
                                    .equals(personService
                                            .getPersonByAuth(authentication)
                                            .getId()))) ||
                    (!writeOthers && note.getNoteSharing()
                            .stream()
                            .map(NoteSharing::getPerson)
                            .anyMatch(person -> person.getId()
                                    .equals(personService
                                            .getPersonByAuth(authentication)
                                            .getId()))))) throw new Exception();
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Don't have rights", e);
        }
    }
}
