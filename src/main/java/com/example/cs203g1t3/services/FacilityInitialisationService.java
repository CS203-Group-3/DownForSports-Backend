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

    public void initialiseFacilities() {
        Facility badminton = new Facility("Badminton Court", "Opens from 10am to 6pm", LocalTime.of(10,0), LocalTime.of(18,0));
        facilityRepository.save(badminton);
        List<TimeSlots> timeSlots = generateTimeSlots(badminton);
        FacilityDate startDate = new FacilityDate(LocalDate.now(), timeSlots);
        startDate.setFacility(badminton);
        // facilityDateRepository.save(startDate); // error 
        List<FacilityDate> facilityDates = generateFacilityDates(startDate);
        badminton.setFacilityDates(facilityDates);
        facilityRepository.save(badminton);
    }

    // Original implementation
        // public List<TimeSlots> generateTimeSlots(Facility facility) {
        //     List<TimeSlots> timeSlots = new ArrayList<TimeSlots>();
        //     LocalTime tempOpen = facility.getOpenTime();
        //     LocalTime tempEnd = facility.getClosingTime();

        //     while(tempOpen.isBefore(tempEnd)){
        //         TimeSlots currentTimeSlot = new TimeSlots(tempOpen, true);
        //         timeSlotsRepository.save(currentTimeSlot);
        //         timeSlots.add(currentTimeSlot);
        //         tempOpen = tempOpen.plusHours(1);
        //     }        
        //     return timeSlots;
        // }

    // New implementation
    public List<TimeSlots> generateTimeSlots(Facility facility) {
        List<TimeSlots> timeSlots = new ArrayList<TimeSlots>();
        LocalTime tempOpen = facility.getOpenTime(); //error > facility is null
        LocalTime tempEnd = facility.getClosingTime();
    
        while (tempOpen.isBefore(tempEnd)) {
            TimeSlots currentTimeSlot = new TimeSlots(tempOpen, true);
            timeSlots.add(currentTimeSlot);
            tempOpen = tempOpen.plusHours(1);
        }
    
        return timeSlots; // Return the detached TimeSlots entities
    }
    
    // Original implementation
    // public List<FacilityDate> generateFacilityDates(FacilityDate startDate) {
    //     List<FacilityDate> facilityDates = new ArrayList<FacilityDate>();
    //     LocalDate tempStart = startDate.getDate();
    //     LocalDate tempEnd = tempStart.plusMonths(1);
    //     List<TimeSlots> timeSlots = startDate.getTimeSlots();

        
    //     while(tempStart.isBefore(tempEnd)){
    //         FacilityDate currentFacilityDate = new FacilityDate(tempStart, timeSlots);
    //         facilityDateRepository.save(currentFacilityDate);
    //         facilityDates.add(currentFacilityDate);
    //         tempStart = tempStart.plusDays(1);
    //     }        

    //     return facilityDates;
    // }

    // New implementation
    public List<FacilityDate> generateFacilityDates(FacilityDate startDate) {
        List<FacilityDate> facilityDates = new ArrayList<FacilityDate>();
        LocalDate tempStart = startDate.getDate();
        LocalDate tempEnd = tempStart.plusMonths(1);
    
        while (tempStart.isBefore(tempEnd)) {
            FacilityDate currentFacilityDate = new FacilityDate(tempStart, new ArrayList<>());
            currentFacilityDate.setFacility(startDate.getFacility());
            List<TimeSlots> timeSlots = generateTimeSlots(startDate.getFacility());
            currentFacilityDate.setTimeSlots(timeSlots);
    
            facilityDateRepository.save(currentFacilityDate);
            facilityDates.add(currentFacilityDate);
            tempStart = tempStart.plusDays(1);
        }
    
        return facilityDates;
    }
    
}
