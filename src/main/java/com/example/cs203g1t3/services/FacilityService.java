package com.example.cs203g1t3.services;

import com.example.cs203g1t3.models.Booking;
import com.example.cs203g1t3.models.Facility;
import com.example.cs203g1t3.models.FacilityDate;
import com.example.cs203g1t3.models.TimeSlots;
import com.example.cs203g1t3.repository.FacilityDateRepository;
import com.example.cs203g1t3.repository.FacilityRepository;
import com.example.cs203g1t3.repository.TimeSlotsRepository;

import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FacilityService {
    private FacilityRepository facilityRepository;
    private TimeSlotsRepository timeSlotsRepository;
    private FacilityDateRepository facilityDateRepository;

    @Autowired
    public FacilityService(FacilityRepository facilityRepository, FacilityDateRepository facilityDateRepository, TimeSlotsRepository timeSlotsRepository) {
        this.facilityRepository = facilityRepository;
        this.facilityDateRepository = facilityDateRepository;
        this.timeSlotsRepository = timeSlotsRepository;
    }

    public List<Facility> listFacilities() {
        return facilityRepository.findAll();
    }

    public Facility getFacility(Long facilityId) {
        return facilityRepository.findById(facilityId).orElse(null);
    }

    public List<Facility> getAllFacilities() {
        return facilityRepository.findAll();
    }


    // public Facility updateFacility(Long facilityId, Facility newFacility) {
    //     return facilityRepository.findById(facilityId).map(facility -> {
    //             facility.setFacilityType(newFacility.getFacilityType());
    //             facility.setDescription(newFacility.getDescription());
    //             facility.setOpenTime(newFacility.getOpenTime());
    //             facility.setClosingTime(newFacility.getClosingTime());
    //             return facilities.save(facility);
    //         }).orElse(null);
    // }

    public void deleteFacility(Long facilityId) {
        facilityRepository.deleteById(facilityId);
    }

    @Transactional
    public void initialiseFacility(Facility facility) {

        List<FacilityDate> facilityDates = generateFacilityDates(LocalDate.now());
        for (FacilityDate currentDate : facilityDates) {
            List<TimeSlots> timeSlots = generateTimeSlots(facility.getOpenTime(), facility.getClosingTime().plusHours(1));
            currentDate.setFacility(facility);
            currentDate.setTimeSlots(timeSlots);
            for (TimeSlots currentTimeSlot : timeSlots) {
                currentTimeSlot.setFacilityDate(currentDate);
                timeSlotsRepository.save(currentTimeSlot);
            }
            // timeSlotsRepository.saveAll(timeSlots);
            facilityDateRepository.save(currentDate);
        }
        facility.setFacilityDates(facilityDates);
        facilityRepository.save(facility);
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

    // public List<FacilityDate> getFacilityDates(Long id) {
    //     Optional<Facility> facilityOptional = facilityRepository.findByFacilityId(id);

    //     return facilityOptional.get().getFacilityDates();
    // }


}
