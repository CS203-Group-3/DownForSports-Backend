package com.example.cs203g1t3.exception;

public class NotEnoughCreditException extends RuntimeException{
    public NotEnoughCreditException(){
        super("Not enough credits");
    }
}
