package com.example.cs203g1t3.exception;

public class InvalidAttendanceStatusException extends RuntimeException{

    public InvalidAttendanceStatusException(String message){
        super(message);
    }
}
