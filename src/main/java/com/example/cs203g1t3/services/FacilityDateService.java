package com.example.cs203g1t3.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cs203g1t3.models.*;
import com.example.cs203g1t3.repository.FacilityDateRepository;

@Service
public class FacilityDateService {
    FacilityDateRepository facilityDateRepository;
    
    @Autowired
    public FacilityDateService(FacilityDateRepository facilityDateRepository){
        this.facilityDateRepository = facilityDateRepository;
    }

    public boolean addTiming(TimeSlots timeslot){
        return true;
    }
}
