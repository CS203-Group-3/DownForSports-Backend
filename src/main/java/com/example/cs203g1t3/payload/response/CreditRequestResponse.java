package com.example.cs203g1t3.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreditRequestResponse {
    private String username;
    private long userID;
    private BookingResponse bookingResponse;
    private int amount;
    private String details;
}
