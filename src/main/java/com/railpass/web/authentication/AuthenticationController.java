package com.railpass.web.authentication;

import java.security.KeyPair;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @PostMapping(path = "/signup", produces = "application/json")
    public AuthenticationResponse login(@Valid @RequestBody AuthenticationRequest request) throws Exception {
        byte[] salt = TokenGenerator.generateSalt();
        byte[] hash = TokenGenerator.generateHash(request.password, salt);
        KeyPair keyPair = TokenGenerator.generateKeyPair(hash);
        String token = TokenGenerator.generateToken(request.username, keyPair.getPrivate());

        return AuthenticationResponse.success(token);
    }
}
