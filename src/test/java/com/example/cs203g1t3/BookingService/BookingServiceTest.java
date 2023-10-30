package com.example.cs203g1t3.BookingService;

import org.springframework.beans.factory.annotation.Autowired;
import java.time.*;
import java.util.*;
import com.example.cs203g1t3.repository.BookingRepository;
import com.example.cs203g1t3.services.FacilityService;
import com.example.cs203g1t3.services.TimeSlotService;

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

public class BookingServiceTest {
    @Autowired
    private BookingService bookingService;
    @Autowired
    private FacilityService facilityService;

    @Test
    void make_booking_valid() throws Exception{
        List<LocalTime> timings = new ArrayList<>();
        timings.add(LocalTime.of(8,0,0));
        timings.add(LocalTime.of(9,0,0));
        BookingRequest bookingRequest = new BookingRequest(1L,1L,LocalDate.of(2023,11,1), timings);

        Facility facility = new Facility("TEST","TEST DESCRIPTION",LocalTime.of(8,0,0),LocalTime.of(11,0,0),1,"Test Location" );
        facilityService.initialiseFacility(facility);


    }

}
