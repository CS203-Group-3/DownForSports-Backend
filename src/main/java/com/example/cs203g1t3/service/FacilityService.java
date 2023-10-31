package com.example.cs203g1t3.service;

import com.example.cs203g1t3.exception.FacilityNotFoundException;
import com.example.cs203g1t3.models.Booking;
import com.example.cs203g1t3.models.FacilityClasses.Facility;
import com.example.cs203g1t3.models.FacilityClasses.FacilityDate;
import com.example.cs203g1t3.models.FacilityClasses.TimeSlots;
import com.example.cs203g1t3.repository.FacilityDateRepository;
import com.example.cs203g1t3.repository.FacilityRepository;
import com.example.cs203g1t3.repository.TimeSlotsRepository;

import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.time.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

public interface FacilityService {

    Long getFacilityDateId(LocalDate date, Long facilityId);

    List<TimeSlots> getAllTimeSlotFromFacility(LocalDate date,Long facilityId);

    List<TimeSlots> getSpecificTimeSlotsAvailable(LocalDate date, Long facilityId);

    void makeTimeSlotsAvailable(List<TimeSlots> timeSlotsList,Long facilityId,LocalDate bookedDate);

    List<Facility> listFacilities();

    Facility getFacility(Long facilityId);

    List<Facility> getAllFacilities();

    void deleteFacility(Long facilityId);

    void initialiseFacility(Facility facility);
    
}
