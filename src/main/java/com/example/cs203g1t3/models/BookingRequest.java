package com.example.cs203g1t3.models;


import java.util.*;
import java.time.*;
import jakarta.persistence.ManyToOne;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.*;



@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class BookingRequest {
    private Long userId;
    private Long facilityId;
    private LocalDate timeBookingMade;

    private LocalDate FacilityDate;
    private List<TimeSlots> TimeSlots;
    
}
