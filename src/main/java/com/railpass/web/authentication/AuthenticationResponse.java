package com.railpass.web.authentication;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationResponse {
    public String token;
    public String error;

    private AuthenticationResponse(String token, String error) {
        this.token = token;
        this.error = error;
    }

    public static AuthenticationResponse success(String token) {
        return new AuthenticationResponse(token, null);
    }

    public static AuthenticationResponse failure(String error) {
        return new AuthenticationResponse(null, error);
    }
}
