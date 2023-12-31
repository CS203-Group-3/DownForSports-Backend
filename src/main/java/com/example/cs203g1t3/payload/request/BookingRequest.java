package com.example.cs203g1t3.payload.request;


import java.util.*;

import com.example.cs203g1t3.models.FacilityClasses.TimeSlots;

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

    private LocalDate FacilityDate;
    private List<LocalTime> TimeSlots;
    
}
