package com.example.cs203g1t3.exception;

public class InvalidCreditRequestException extends RuntimeException{

    public InvalidCreditRequestException() {
        super("Invalid request amount");
    }
}
