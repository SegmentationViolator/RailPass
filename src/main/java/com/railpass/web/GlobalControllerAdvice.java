package com.railpass.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler(NoHandlerFoundException.class)
	public String notFound(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		return "forward:/static/index.html";
	}

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<String> missingHeader(HttpServletResponse response) {
        return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
    }
}
