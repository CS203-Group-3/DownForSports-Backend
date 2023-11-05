package com.example.cs203g1t3.BookingService;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.time.*;
import java.util.*;


import com.example.cs203g1t3.repository.BookingRepository;
import com.example.cs203g1t3.repository.FacilityDateRepository;
import com.example.cs203g1t3.repository.FacilityRepository;
import com.example.cs203g1t3.repository.TimeSlotsRepository;

import jakarta.inject.Inject;

import com.example.cs203g1t3.service.*;
import com.example.cs203g1t3.servicesImpl.BookingServiceImpl;
import com.example.cs203g1t3.servicesImpl.FacilityServiceImpl;
import com.example.cs203g1t3.models.*;
import com.example.cs203g1t3.models.FacilityClasses.*;

import com.example.cs203g1t3.payload.request.BookingRequest;
import com.example.cs203g1t3.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {
    @Mock
    private BookingServiceImpl bookingService;
    @InjectMocks
    private FacilityServiceImpl facilityService;
    @Mock
    private TimeSlotsRepository timeSlotsRepository;
    @Mock
    private FacilityDateRepository facilityDateRepository;
    @Mock 
    private FacilityRepository facilityRepository;

    
    @Test
    void make_booking_valid_Booking_Return_Booking() throws Exception{
        User user = new User("S1325847C", "Email@email.com","Password");
        user.setUserID(1L);
        LocalDate dateBooked = LocalDate.now().plusDays(1);
        int startHour = LocalTime.now().getHour();
        int endHour = LocalTime.now().plusHours(1).getHour();
        Facility facility = new Facility("TEST NAME","TEST DESCRIPTION",LocalTime.of(startHour, 0),LocalTime.of(endHour, 0),50,"Test Location" );
        facility.setFacilityId(1L);
        
        List<TimeSlots> timeSlotsList = new ArrayList<>();
        TimeSlots timeSlots = new TimeSlots(LocalTime.of(startHour, 0), true);
        timeSlots.setTimeSlotsId(1L);
        timeSlotsList.add(timeSlots);

        FacilityDate faciDate = new FacilityDate(dateBooked, timeSlotsList);
        faciDate.setFacilityDateId(1L);
        timeSlots.setFacilityDate(faciDate);

        List<FacilityDate> faciDateList = new ArrayList<>();
        faciDateList.add(faciDate);
        facility.setFacilityDates(faciDateList);

        List<LocalTime> timings = new ArrayList<>();
        timings.add(LocalTime.of(startHour,0));
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        BookingRequest bookingRequest = new BookingRequest(1L,1L,dateBooked, timings);
        
        // Booking booking = new Booking(LocalTime.of(startHour,0), LocalTime.of(endHour,0),LocalDateTime.now(),(double)timings.size()*facility.getCreditCost());
    
        // when(facility.getFacilityDates()).thenReturn(faciDateList);
        when(facilityService.getAllTimeSlotFromFacility(dateBooked,facility.getFacilityId())).thenReturn(timeSlotsList);

        Booking booking2 = bookingService.makeBooking(bookingRequest);

                System.setOut(new PrintStream(outContent) );

        // assertEquals(500, user.getCreditScore());
        // assertNotNull(booking2);
        verify(facilityService.getAllTimeSlotFromFacility(dateBooked,facility.getFacilityId()));
    }   

}
