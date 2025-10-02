package com.railpass.web.authentication;

import jakarta.validation.constraints.NotBlank;

public class AuthenticationRequest {
    @NotBlank
    public String email;

    @NotBlank
    public String password;
}
