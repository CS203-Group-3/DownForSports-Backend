package com.example.cs203g1t3.facility;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import com.example.cs203g1t3.models.FacilityClasses.Facility;
import com.example.cs203g1t3.models.FacilityClasses.FacilityDate;
import com.example.cs203g1t3.models.FacilityClasses.TimeSlots;
import com.example.cs203g1t3.repository.FacilityDateRepository;
import com.example.cs203g1t3.repository.FacilityRepository;
import com.example.cs203g1t3.repository.TimeSlotsRepository;
import com.example.cs203g1t3.servicesImpl.FacilityServiceImpl;

@ExtendWith(MockitoExtension.class)
public class FacilityServiceTest {
    @Mock
    private FacilityRepository facilities;

    @Mock
    private TimeSlotsRepository timeslots;

    @Mock
    private FacilityDateRepository facilityDates;

    @InjectMocks
    private FacilityServiceImpl facilityService;


    @Test
    void getAllTimeSlotFromFacility_FacilityFound_ReturnListAllTimeSlots() {
        LocalDate date = LocalDate.now();
        Facility facility = new Facility();
        facility.setFacilityId(10L);
        FacilityDate facilityDate = new FacilityDate(date, new ArrayList<>());
        TimeSlots timeSlot1 = new TimeSlots(LocalTime.of(10, 0), true);
        TimeSlots timeSlot2 = new TimeSlots(LocalTime.of(18, 0), false);
        facilityDate.setTimeSlots(List.of(timeSlot1, timeSlot2));
        facility.setFacilityDates(List.of(facilityDate));
        when(facilities.findById(10L)).thenReturn(Optional.of(facility));

     List<TimeSlots> timeSlots = facilityService.getAllTimeSlotFromFacility(date, 10L);

        assertNotNull(timeSlots);
        assertEquals(2, timeSlots.size());
    }

    @Test
    void getAllTimeSlotFromFacility_FacilityNotFound_ReturnNull() {
        LocalDate date = LocalDate.now();

        when(facilities.findById(10L)).thenReturn(Optional.empty());

        List<TimeSlots> actual = facilityService.getAllTimeSlotFromFacility(date, 10L);
        assertNull(actual);
        
    }

    
    @Test
    public void getSpecificTimeSlotsAvailable_AllBooked_ReturnEmptyList() {
        LocalDate date = LocalDate.now();
        Facility facility = new Facility("Test Facility", "Test description", LocalTime.of(10, 0), LocalTime.of(18, 0), 0, "Test");
        facility.setFacilityId(10L);
        List<FacilityDate> facilityDates = new ArrayList<>();
        List<TimeSlots> timeSlots = new ArrayList<>();

        facilityDates.add(new FacilityDate(date, timeSlots));
        LocalTime openTime = facility.getOpenTime();

        while (openTime.isBefore(facility.getClosingTime())) {
            timeSlots.add(new TimeSlots(openTime, false));
            openTime = openTime.plusHours(1);
        }

        facility.setFacilityDates(facilityDates);

        when(facilities.findById(10L)).thenReturn(Optional.of(facility));

        List<TimeSlots> availableTimeSlots = facilityService.getSpecificTimeSlotsAvailable(date, 10L);

        assertEquals(0, availableTimeSlots.size());
        assertEquals(new ArrayList<TimeSlots>(), availableTimeSlots);
    }

    @Test
    public void getSpecificTimeSlotsAvailable_SomeBooked_ReturnSpecificList() {
        LocalDate date = LocalDate.now();
        Facility facility = new Facility("Test Facility", "Test description", LocalTime.of(10, 0), LocalTime.of(18, 0), 0, "Test");
        facility.setFacilityId(10L);
        List<FacilityDate> facilityDates = new ArrayList<>();
        List<TimeSlots> timeSlots = new ArrayList<>();

        facilityDates.add(new FacilityDate(date, timeSlots));
        LocalTime openTime = facility.getOpenTime();
        
        // Booked first timeslot
        timeSlots.add(new TimeSlots(openTime, false));
        openTime = openTime.plusHours(1);

        // Other 7 timeslots not booked, available.
        while (openTime.isBefore(facility.getClosingTime())) {
            timeSlots.add(new TimeSlots(openTime, true));
            openTime = openTime.plusHours(1);
        }

        facility.setFacilityDates(facilityDates);

        when(facilities.findById(10L)).thenReturn(Optional.of(facility));

        List<TimeSlots> availableTimeSlots = facilityService.getSpecificTimeSlotsAvailable(date, 10L);

        // 8 timeslots from 1000 to 1800, 1 of which is booked. Hence 7 available timeslots.
        assertEquals(7, availableTimeSlots.size());
    }

    
}
