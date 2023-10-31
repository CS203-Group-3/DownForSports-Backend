package com.example.cs203g1t3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.cs203g1t3.models.FacilityClasses.TimeSlots;

@Repository
public interface TimeSlotsRepository extends JpaRepository<TimeSlots, Long> {
    
}
