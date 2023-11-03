package com.example.cs203g1t3.payload.response;
import java.util.Comparator;
import java.util.Collections;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookingResponse {
    private long bookingId;
    private String facility;
    private String description;
    private String startTime;
    private String endTime;
    private String date;
    private String location;
    private Boolean bookingAttendanceChecked;
}
