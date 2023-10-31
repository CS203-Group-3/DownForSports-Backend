package com.example.cs203g1t3.BookingService;

import org.springframework.beans.factory.annotation.Autowired;
import java.time.*;
import java.util.*;
import com.example.cs203g1t3.repository.BookingRepository;
import com.example.cs203g1t3.repository.FacilityDateRepository;
import com.example.cs203g1t3.repository.FacilityRepository;
import com.example.cs203g1t3.repository.TimeSlotsRepository;

import jakarta.inject.Inject;

import com.example.cs203g1t3.services.*;
import com.example.cs203g1t3.models.Facility;
import com.example.cs203g1t3.models.User;
import com.example.cs203g1t3.payload.request.BookingRequest;
import com.example.cs203g1t3.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {
    @Autowired
    private BookingService bookingService;
    @InjectMocks
    private FacilityService facilityService;
    @Mock
    private TimeSlotsRepository timeSlotsRepository;
    @Mock
    private FacilityDateRepository facilityDateRepository;
    @Mock 
    private FacilityRepository facilityRepository;

    
    @Test
    void make_booking_valid() throws Exception{
        List<LocalTime> timings = new ArrayList<>();
        timings.add(LocalTime.of(8,0,0));
        timings.add(LocalTime.of(9,0,0));
        BookingRequest bookingRequest = new BookingRequest(1L,1L,LocalDate.of(2023,11,1), timings);

        //Initialise facility with opening time 8am, closing time 11am
        Facility facility = new Facility("TEST NAME","TEST DESCRIPTION",LocalTime.of(8,0,0),LocalTime.of(11,0,0),1,"Test Location" );
        facilityService.initialiseFacility(facility);

        
    }   

}
