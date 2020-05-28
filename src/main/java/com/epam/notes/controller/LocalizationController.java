package com.epam.notes.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.ResourceBundle;

@RestController
@RequestMapping("/api")
public class LocalizationController {
    @GetMapping("/setLocale")
    public MyBundle getBundle(@RequestParam String locale) {
       return new MyBundle(locale);
    }
}

class MyBundle {
    private final ResourceBundle bundle;

    public MyBundle(String s) {
        Locale current =  s.equals("ru") ? new Locale("ru", "RU")
                : new Locale("en", "US");
        this.bundle = ResourceBundle.getBundle("text", current);
    }

    private String decode(String string) {
        try {
            return new String(string.getBytes(StandardCharsets.ISO_8859_1), "Windows-1251");
        } catch (UnsupportedEncodingException e) {
            e.getStackTrace();
            return null;
        }
    }

    public String getLogin() {
        return decode(bundle.getString("login"));
    }

    public String getSignIn() {
        return decode(bundle.getString("signIn"));
    }

    public String getSignUp() {
        return decode(bundle.getString("signUp"));
    }

    public String getName() {
        return decode(bundle.getString("name"));
    }

    public String getPassword() {
        return decode(bundle.getString("password"));
    }

    public String getNotes() {
        return decode(bundle.getString("notes"));
    }

    public String getBasket() {
        return decode(bundle.getString("basket"));
    }

    public String getLogout() {
        return decode(bundle.getString("logout"));
    }

    public String getCreateNewNote() {
        return decode(bundle.getString("createNewNote"));
    }

    public String getImportNote() {
        return decode(bundle.getString("importNote"));
    }

    public String getUpload() {
        return decode(bundle.getString("upload"));
    }

    public String getMyNotes() {
        return decode(bundle.getString("myNotes"));
    }

    public String getTitle() {
        return decode(bundle.getString("title"));
    }

    public String getCreated() {
        return decode(bundle.getString("created"));
    }

    public String getEdited() {
        return decode(bundle.getString("edited"));
    }

    public String getActions() {
        return decode(bundle.getString("actions"));
    }

    public String getShare() {
        return decode(bundle.getString("share"));
    }

    public String getOpen() {
        return decode(bundle.getString("open"));
    }

    public String getDownload() {
        return decode(bundle.getString("download"));
    }

    public String getDelete() {
        return decode(bundle.getString("delete"));
    }

    public String getOtherNotes() {
        return decode(bundle.getString("otherNotes"));
    }

    public String getDeletedNotes() {
        return decode(bundle.getString("deletedNotes"));
    }

    public String getHistory() {
        return decode(bundle.getString("history"));
    }

    public String getSave() {
        return decode(bundle.getString("save"));
    }

    public String getUsername() {
        return decode(bundle.getString("username"));
    }

    public String getNotestitle() {
        return decode(bundle.getString("notestitle"));
    }

    public String getCreate() {
        return decode(bundle.getString("create"));
    }

}
