package com.example.cs203g1t3.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cs203g1t3.models.FacilityClasses.Facility;

import java.util.List;
import java.util.Optional;


public interface FacilityRepository extends JpaRepository<Facility, Long> {
    Optional<Facility> findByFacilityId(Long facilityId);
}
