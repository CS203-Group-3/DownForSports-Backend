package com.example.cs203g1t3.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class TimeSlotNotFound extends RuntimeException{

        private static final long serialVersionUID = 1L;

    public TimeSlotNotFound(){
        super("Could not find timeslot");
    }
}



