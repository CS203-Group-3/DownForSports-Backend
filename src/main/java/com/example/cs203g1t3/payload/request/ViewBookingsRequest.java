package com.example.cs203g1t3.payload.request;

import javax.validation.constraints.NotBlank;

public class ViewBookingsRequest {
    @NotBlank
    Long userId;

    public Long getUserId(){
        return userId;
    }
}
