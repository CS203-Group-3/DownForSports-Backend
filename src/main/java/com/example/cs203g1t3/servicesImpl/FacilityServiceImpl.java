package com.example.cs203g1t3.servicesImpl;

import com.example.cs203g1t3.exception.FacilityNotFoundException;
import com.example.cs203g1t3.models.Booking;
import com.example.cs203g1t3.models.FacilityClasses.Facility;
import com.example.cs203g1t3.models.FacilityClasses.FacilityDate;
import com.example.cs203g1t3.models.FacilityClasses.TimeSlots;
import com.example.cs203g1t3.repository.FacilityDateRepository;
import com.example.cs203g1t3.repository.FacilityRepository;
import com.example.cs203g1t3.repository.TimeSlotsRepository;
import com.example.cs203g1t3.service.FacilityService;

import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.time.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FacilityServiceImpl implements FacilityService {
    private FacilityRepository facilityRepository;
    private TimeSlotsRepository timeSlotsRepository;
    private FacilityDateRepository facilityDateRepository;

    @Autowired
    public FacilityServiceImpl(FacilityRepository facilityRepository, FacilityDateRepository facilityDateRepository, TimeSlotsRepository timeSlotsRepository) {
        this.facilityRepository = facilityRepository;
        this.facilityDateRepository = facilityDateRepository;
        this.timeSlotsRepository = timeSlotsRepository;
    }

    public Long getFacilityDateId(LocalDate date, Long facilityId){
        Facility facility = getFacility(facilityId);
        //checking if facility exist
        if(facility == null){
            return null;
        }

        List<FacilityDate> facilityDates = facility.getFacilityDates();
        for(FacilityDate fd: facilityDates){
            if(fd.getDate().equals(date)){
                return fd.getFacilityDateId();
            }
        }

        return null;
    }

    public List<TimeSlots> getAllTimeSlotFromFacility(LocalDate date,Long facilityId){
        Facility facility = getFacility(facilityId);
        //checking if facility exist
        if(facility == null){
            return null;
        }

        List<FacilityDate> facilityDates = facility.getFacilityDates();
        for(FacilityDate fd: facilityDates){
            if(fd.getDate().equals(date)){
                return fd.getTimeSlots();
            }
        }
        
        return null;
    }
    public List<TimeSlots> getSpecificTimeSlotsAvailable(LocalDate date, Long facilityId){
        Facility facility = getFacility(facilityId);

        //checking if facility exist
        if(facility == null){
            return null;
        }

        //Get the List<FacilityDate> in facility object
        List<FacilityDate> facilitydatesList = facility.getFacilityDates();

        //Creating list to save TimeSlot into a list and output when done
        List<TimeSlots> availableTimeSlotsList = new ArrayList<>();

        //Loop through List<FacilityDate> in the facility
        for(FacilityDate facilityDate: facilitydatesList){

            //check if facilityDate LocalDate is the same as the parameter LocalDate
            if(facilityDate.getDate().equals(date)){

                //Loop through List<TimeSlot>
                for(TimeSlots timings:facilityDate.getTimeSlots()){

                    //Checking if timeslot is available, it returns false if it is taken
                    if(timings.isAvailable()){
                        availableTimeSlotsList.add(timings);
                    }
                }
            }
        }
        return availableTimeSlotsList;
    }

    public void makeTimeSlotsAvailable(List<TimeSlots> timeSlotsList,Long facilityId,LocalDate bookedDate){
        Facility facility = getFacility(facilityId);
        //checking if facility exist
        if(facility == null){
            throw new FacilityNotFoundException(facilityId);
        }

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
