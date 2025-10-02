package com.railpass.web.authentication;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.railpass.web.User;
import com.railpass.web.UserRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    private UserRepository repository;

    @PostMapping(path = "/signup", produces = "application/json")
    public AuthenticationResponse signup(@Valid @RequestBody AuthenticationRequest request) {
        byte[] salt = PasswordHasher.generateSalt();
        byte[] hash = PasswordHasher.generateHash(request.password, salt);

        if (repository.existsByEmail(request.email)) return AuthenticationResponse.failure("user already exists");
        
        User user = new User(request.email, hash, salt);
        repository.save(user);

        String token = TokenGenerator.generateToken(user.getId().toString());

        return AuthenticationResponse.success(token);
    }

    @PostMapping(path = "/login", produces = "application/json")
    public AuthenticationResponse login(@Valid @RequestBody AuthenticationRequest request) {
        Optional<User> optionalUser = repository.findByEmail(request.email);

        if (optionalUser.isEmpty()) return AuthenticationResponse.failure("user not found");

        User user = optionalUser.get();
        byte[] salt = user.getSalt();
        byte[] hash = PasswordHasher.generateHash(request.password, salt);

        if (!Arrays.equals(user.getPasswordHash(), hash)) return AuthenticationResponse.failure("incorrect password");

        String token = TokenGenerator.generateToken(user.getId().toString());

        return AuthenticationResponse.success(token);
    }
}
