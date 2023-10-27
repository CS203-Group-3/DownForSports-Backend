package com.example.cs203g1t3.services;

import org.springframework.stereotype.Service;

import com.example.cs203g1t3.exception.TimeSlotNotFound;
import com.example.cs203g1t3.models.TimeSlots;
import com.example.cs203g1t3.repository.TimeSlotsRepository;

import jakarta.transaction.Transactional;

@Service
public class TimeSlotService {
    private TimeSlotsRepository timeSlotsRepository;
    
    public TimeSlotService(TimeSlotsRepository timeSlotsRepository){
        this.timeSlotsRepository = timeSlotsRepository;
    }

    @Transactional
    public TimeSlots updateToUnavailable(Long timeSlotId){
        return  timeSlotsRepository.findById(timeSlotId).map(timeslot -> {
            timeslot.setAvailable(false);
            return timeSlotsRepository.save(timeslot);
        }).orElse(null);
    }

    @Transactional
    public TimeSlots updateToAvailable(Long timeSlotId){
        return  timeSlotsRepository.findById(timeSlotId).map(timeslot -> {
            timeslot.setAvailable(true);
            return timeSlotsRepository.save(timeslot);
        }).orElse(null);
    }

}
