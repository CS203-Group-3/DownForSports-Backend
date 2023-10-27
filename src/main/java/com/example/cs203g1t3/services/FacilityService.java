package com.example.cs203g1t3.services;

import com.example.cs203g1t3.exception.FacilityNotFoundException;
import com.example.cs203g1t3.models.Booking;
import com.example.cs203g1t3.models.*;
import com.example.cs203g1t3.models.TimeSlots;
import com.example.cs203g1t3.repository.FacilityRepository;

import jakarta.transaction.Transactional;

import java.util.*;
import java.time.*;

import org.springframework.stereotype.Service;

@Service
public class FacilityService {
    private FacilityRepository facilities;
    // private FacilityDateService facilityDateService;
    public FacilityService(FacilityRepository facilities) {
        this.facilities = facilities;
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
        return facilities.findAll();
    }

    public Facility getFacility(Long facilityId) {
        return facilities.findById(facilityId).orElse(null);
    }

    public List<Facility> getAllFacilities() {
        return facilities.findAll();
    }

    public Facility createFacility(Facility facility) {
        return facilities.save(facility);
    }


    public Facility updateFacility(Long facilityId, Facility newFacility) {
        return facilities.findById(facilityId).map(facility -> {
                facility.setFacilityType(newFacility.getFacilityType());
                facility.setDescription(newFacility.getDescription());
                facility.setOpenTime(newFacility.getOpenTime());
                facility.setClosingTime(newFacility.getClosingTime());
                return facilities.save(facility);
            }).orElse(null);
    }

    public void deleteFacility(Long facilityId) {
        facilities.deleteById(facilityId);
    }

    
}
