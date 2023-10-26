package com.example.cs203g1t3.payload.response;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacilityResponse {

    private String facilityType;
    private String description;

    private LocalTime openTime;
    private LocalTime closingTime;
    
    private int creditCost; 
    private String locationString;

}
