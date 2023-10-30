package com.example.cs203g1t3.services;

import org.springframework.stereotype.Service;

import com.example.cs203g1t3.models.TimeSlots;
import com.example.cs203g1t3.repository.TimeSlotsRepository;

import jakarta.transaction.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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

   public List<TimeSlots> filterUpcomingTimeSlotsForDay(List<TimeSlots> input){
        LocalTime timeNow = LocalTime.now();
        List<TimeSlots> result = new ArrayList<>();
        for(TimeSlots i : input){
            if(timeNow.isAfter(i.getStartTime())){
                continue;
            }
            if(timeNow.equals(i.getStartTime())){
                continue;
            }
            result.add(i);
        }
        return result;
   }

   public List<TimeSlots> filterPastTimeSlotsForDay(List<TimeSlots> input){
        LocalTime timeNow = LocalTime.now();
        List<TimeSlots> result = new ArrayList<>();
        for(TimeSlots i : input){
            if(timeNow.isBefore(i.getStartTime())){
                continue;
            }
            result.add(i);
        }
        return result;
   }
}
