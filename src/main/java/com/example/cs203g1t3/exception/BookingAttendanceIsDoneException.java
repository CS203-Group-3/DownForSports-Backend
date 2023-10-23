package com.example.cs203g1t3.exception;

public class BookingAttendanceIsDoneException extends RuntimeException{

    public BookingAttendanceIsDoneException(String message){
        super(message);
    }
}
