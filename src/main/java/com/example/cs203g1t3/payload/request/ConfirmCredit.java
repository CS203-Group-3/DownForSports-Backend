package com.example.cs203g1t3.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConfirmCredit {

    long userID;
    int amount;
    long creditRequestID;
}
