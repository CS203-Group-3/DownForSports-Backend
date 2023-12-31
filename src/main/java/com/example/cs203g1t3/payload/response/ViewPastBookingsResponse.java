package com.example.cs203g1t3.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ViewPastBookingsResponse {
    private String facility;
    private String description;
    private String startTime;
    private String endTime;
    private String date;
    private String location;
}
