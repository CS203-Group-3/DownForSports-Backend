package com.example.cs203g1t3.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import com.example.cs203g1t3.models.Facility;
import com.example.cs203g1t3.models.FacilityDate;
import com.example.cs203g1t3.models.TimeSlots;
import com.example.cs203g1t3.repository.FacilityDateRepository;
import com.example.cs203g1t3.repository.FacilityRepository;
import com.example.cs203g1t3.repository.TimeSlotsRepository;

import jakarta.transaction.Transactional;


@Service
public class FacilityInitialisationService {
    private FacilityRepository facilityRepository;
    private TimeSlotsRepository timeSlotsRepository;
    private FacilityDateRepository facilityDateRepository;

    @Autowired
    public FacilityInitialisationService(FacilityRepository facilityRepository, FacilityDateRepository facilityDateRepository, TimeSlotsRepository timeSlotsRepository) {
        this.facilityRepository = facilityRepository;
        this.facilityDateRepository = facilityDateRepository;
        this.timeSlotsRepository = timeSlotsRepository;
    }

    @Transactional
    public void initialiseFacilities() {
        Facility badminton = new Facility("Badminton Court", "Opens from 10am to 6pm", LocalTime.of(10,0), LocalTime.of(18,0));
        List<FacilityDate> facilityDates = generateFacilityDates(LocalDate.now());
        for (FacilityDate currentDate : facilityDates) {
            List<TimeSlots> timeSlots = generateTimeSlots(badminton.getOpenTime(), badminton.getClosingTime().plusHours(1));
            currentDate.setFacility(badminton);
            currentDate.setTimeSlots(timeSlots);
            for (TimeSlots currentTimeSlot : timeSlots) {
                currentTimeSlot.setFacilityDate(currentDate);
                timeSlotsRepository.save(currentTimeSlot);
            }
            // timeSlotsRepository.saveAll(timeSlots);
            facilityDateRepository.save(currentDate);
        }
        badminton.setFacilityDates(facilityDates);
        facilityRepository.save(badminton);
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
        LocalDate tempEnd = tempStart.plusMonths(1);
    
        while (tempStart.isBefore(tempEnd)) {
            FacilityDate currentFacilityDate = new FacilityDate(tempStart, new ArrayList<>());
            facilityDates.add(currentFacilityDate);
            tempStart = tempStart.plusDays(1);
        }
    
        return facilityDates;
    }
    
}
