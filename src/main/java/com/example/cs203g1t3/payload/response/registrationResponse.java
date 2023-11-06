package com.example.cs203g1t3.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class registrationResponse {
    MessageResponse messageResponse;
    Long userID;
}
