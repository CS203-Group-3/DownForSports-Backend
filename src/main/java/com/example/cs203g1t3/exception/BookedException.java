package com.example.cs203g1t3.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class BookedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public BookedException(String message) {
        super(message);
    }
    

}
