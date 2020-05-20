package com.epam.notes.controller;


import com.epam.notes.entity.Person;
import com.epam.notes.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
public class AuthenticationController {
    @Autowired
    private PersonService personService;

    @GetMapping("/logout")
    public LoginResponse logout(HttpServletRequest request,
                                HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        session.invalidate();
        this.killCookies(response, request);
        return new LoginResponse(HttpServletResponse.SC_OK, "Unauthorized");
    }

    @PostMapping(value = "/login", produces = {"application/json"})
    public LoginResponse login(@RequestBody Person user,
                               HttpServletRequest request,
                               HttpServletResponse response) throws ServletException {
        if (user.getName() == null || user.getPassword() == null) {
            return fail(response, request);
        }
        try {
            Person person = personService.getPersonByName(user.getName());
            if (person == null) return fail(response, request);
            if (request.getRemoteUser() != null) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication != null)
                    new SecurityContextLogoutHandler().logout(request, response, authentication);
            }
            request.login(user.getName(), user.getPassword());
            this.setCookies(response, request);
            return new LoginResponse(HttpServletResponse.SC_OK, "Authorized");
        } catch (ServletException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            this.killCookies(response, request);
            return new LoginResponse(response.getStatus(), "Unauthorized");
        }
    }

    private LoginResponse fail(HttpServletResponse response, HttpServletRequest request) {
        response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        this.killCookies(response, request);
        return new LoginResponse(response.getStatus(), "Unauthorized");
    }

    private void setCookies(HttpServletResponse response, HttpServletRequest request) {
        HttpSession httpSession = request.getSession();
        Cookie cookie = new Cookie("JSESSIONID", httpSession.getId());
        cookie.setHttpOnly(false);
        cookie.setSecure(false);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    private void killCookies(HttpServletResponse response, HttpServletRequest request) {
        for (Cookie cookie : request.getCookies()) {
            cookie.setHttpOnly(true);
            cookie.setSecure(false);
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }

    public static class LoginResponse {
        private final int code;
        private final String message;

        public LoginResponse(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }
}
