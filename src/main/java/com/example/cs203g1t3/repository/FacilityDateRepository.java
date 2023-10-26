package com.example.cs203g1t3.repository;

import com.example.cs203g1t3.models.Facility;
import com.example.cs203g1t3.models.FacilityDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacilityDateRepository extends JpaRepository<FacilityDate, Long> {
    
}
