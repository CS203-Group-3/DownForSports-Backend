package com.example.cs203g1t3.repository;

import com.example.cs203g1t3.models.Facility;


import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;


public interface FacilityRepository extends JpaRepository<Facility, Long> {
    Optional<Facility> findByFacilityId(Long facilityId);
}
