package com.example.cs203g1t3.services;

import org.springframework.stereotype.Service;

import com.example.cs203g1t3.exception.TimeSlotNotFound;
import com.example.cs203g1t3.models.TimeSlots;
import com.example.cs203g1t3.repository.TimeSlotsRepository;

@Service
public class TimeSlotService {
    private TimeSlotsRepository timeSlotsRepository;
    
    public TimeSlotService(TimeSlotsRepository timeSlotsRepository){
        this.timeSlotsRepository = timeSlotsRepository;
    }

    public void updateToUnavailable(Long timeSlotId){
        TimeSlots timeslot = timeSlotsRepository.findById(timeSlotId).orElse(null);
        if(timeslot == null){
            throw new TimeSlotNotFound(timeSlotId);
        }
        timeslot.setAvailable(false);
        timeSlotsRepository.save(timeslot);
    }
}
