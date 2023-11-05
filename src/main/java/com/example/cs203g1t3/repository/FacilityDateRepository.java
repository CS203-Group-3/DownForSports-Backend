package com.example.cs203g1t3.repository;

import com.example.cs203g1t3.models.FacilityClasses.Facility;
import com.example.cs203g1t3.models.FacilityClasses.FacilityDate;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface FacilityDateRepository extends JpaRepository<FacilityDate, Long> {
    
}
