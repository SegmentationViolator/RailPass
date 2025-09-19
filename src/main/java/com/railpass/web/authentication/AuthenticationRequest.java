package com.railpass.web.authentication;

import jakarta.validation.constraints.NotBlank;

public class AuthenticationRequest {
    @NotBlank
    public String username;

    @NotBlank
    public String password;
}
