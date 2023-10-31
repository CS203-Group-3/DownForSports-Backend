package com.example.cs203g1t3.servicesImpl;

import java.time.*;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cs203g1t3.models.*;
import com.example.cs203g1t3.models.FacilityClasses.FacilityDate;
import com.example.cs203g1t3.repository.FacilityDateRepository;
import com.example.cs203g1t3.repository.TimeSlotsRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class FacilityDateService {
    FacilityDateRepository facilityDateRepository;
    TimeSlotService timeSlotService;
    @Autowired
    public FacilityDateService(FacilityDateRepository facilityDateRepository,    TimeSlotService timeSlotService){
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
