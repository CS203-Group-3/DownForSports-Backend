package com.example.cs203g1t3.payload.request;

import javax.validation.constraints.NotBlank;

public class CancelBookingRequest {
    @NotBlank
    private Long bookingId;

    public long getBookingId(){
        return bookingId;
    }
}
