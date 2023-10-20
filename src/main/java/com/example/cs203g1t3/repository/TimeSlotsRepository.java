package com.example.cs203g1t3.repository;

import com.example.cs203g1t3.models.TimeSlots;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeSlotsRepository extends JpaRepository<TimeSlots, Long> {
    
}
