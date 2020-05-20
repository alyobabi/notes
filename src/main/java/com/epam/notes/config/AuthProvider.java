package com.epam.notes.config;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class AuthProvider implements AuthenticationProvider {

    private final UserWithAuthoritiesService userWithAuthoritiesService;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthProvider(UserWithAuthoritiesService userWithAuthoritiesService, BCryptPasswordEncoder passwordEncoder) {
        this.userWithAuthoritiesService = userWithAuthoritiesService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = (String)authentication.getCredentials();
        if (password == null) {
            throw new BadCredentialsException("Password is empty");
        }
        User user = userWithAuthoritiesService.loadUserByUsername(name);
        if (user == null) throw new BadCredentialsException("Username not found");
        if (passwordEncoder.encode(password).equals(user.getPassword())) throw new BadCredentialsException("Wronf password");
        return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
