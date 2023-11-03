package com.example.cs203g1t3.exception;

public class InvalidUserException extends RuntimeException{

    public InvalidUserException(String e) {
        super(e);
    }
}
