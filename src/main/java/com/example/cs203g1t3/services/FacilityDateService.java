package com.example.cs203g1t3.services;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cs203g1t3.models.*;
import com.example.cs203g1t3.repository.FacilityDateRepository;
import com.example.cs203g1t3.repository.TimeSlotsRepository;

@Service
public class FacilityDateService {
    FacilityDateRepository facilityDateRepository;
    TimeSlotService timeSlotService;
    @Autowired
    public FacilityDateService(FacilityDateRepository facilityDateRepository,    TimeSlotService timeSlotService
){
        this.facilityDateRepository = facilityDateRepository;
        this.timeSlotService = timeSlotService;
    }

    // public boolean addTiming(TimeSlots timeslot){
    //     return true;
    // }
        
    public FacilityDate getFacilityDate(Long facilityDateId){
        return facilityDateRepository.findById(facilityDateId).orElse(null);
    }
    
}