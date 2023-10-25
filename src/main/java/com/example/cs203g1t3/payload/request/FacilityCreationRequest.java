package com.example.cs203g1t3.payload.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacilityCreationRequest {
    private String facilityType;
    private String description;

    // Pass back a string of {hh:mm}
    private String openTime;
    private String closingTime;
    
    private String creditCost; 
    private String locationString;

}
