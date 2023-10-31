package com.example.cs203g1t3.servicesImpl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import com.example.cs203g1t3.models.FacilityClasses.Facility;
import com.example.cs203g1t3.models.FacilityClasses.FacilityDate;
import com.example.cs203g1t3.models.FacilityClasses.TimeSlots;
import com.example.cs203g1t3.repository.FacilityDateRepository;
import com.example.cs203g1t3.repository.FacilityRepository;
import com.example.cs203g1t3.repository.TimeSlotsRepository;
import com.example.cs203g1t3.service.FacilityService;

import jakarta.transaction.Transactional;


@Service
@Transactional
public class FacilityInitialisationService {
    private FacilityService facilityService;
    private FacilityRepository facilityRepository;
    private TimeSlotsRepository timeSlotsRepository;
    private FacilityDateRepository facilityDateRepository;
    private TimeSlotService tss;
    @Autowired
    public FacilityInitialisationService(FacilityRepository facilityRepository, FacilityDateRepository facilityDateRepository, TimeSlotsRepository timeSlotsRepository, FacilityService facilityService) {
        this.facilityRepository = facilityRepository;
        this.facilityDateRepository = facilityDateRepository;
        this.timeSlotsRepository = timeSlotsRepository;
        this.facilityService = facilityService;
    }

    @Transactional
    public void initialiseFacilities() {
        Facility badminton = new Facility("Badminton Court", "Opens from 10am to 6pm", LocalTime.of(10,0), LocalTime.of(18,0), 50, "SMU");
        facilityService.initialiseFacility(badminton);
    }

    // New implementation
    public List<TimeSlots> generateTimeSlots(LocalTime openTime, LocalTime closingTime) {
        List<TimeSlots> timeSlots = new ArrayList<TimeSlots>();
        LocalTime tempOpen = openTime;
        LocalTime tempEnd = closingTime;
    
        while (tempOpen.isBefore(tempEnd)) {
            TimeSlots currentTimeSlot = new TimeSlots(tempOpen, true);
            timeSlots.add(currentTimeSlot);
            tempOpen = tempOpen.plusHours(1);
        }
    
        return timeSlots; 
    }

    // New implementation
    public List<FacilityDate> generateFacilityDates(LocalDate startDate) {
        List<FacilityDate> facilityDates = new ArrayList<FacilityDate>();
        LocalDate tempStart = startDate;
        LocalDate tempEnd = tempStart.plusDays(1);
    
        while (tempStart.isBefore(tempEnd)) {
            FacilityDate currentFacilityDate = new FacilityDate(tempStart, new ArrayList<>());
            facilityDates.add(currentFacilityDate);
            tempStart = tempStart.plusDays(1);
        }
    
        return facilityDates;
    }
    
}
