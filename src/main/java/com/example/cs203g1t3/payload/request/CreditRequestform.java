package com.example.cs203g1t3.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreditRequestform {
    private int amount;
    private String details;
    private Long bookingId;
}
