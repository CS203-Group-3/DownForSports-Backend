package com.example.cs203g1t3.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cs203g1t3.models.FacilityClasses.Facility;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface FacilityRepository extends JpaRepository<Facility, Long> {
    Optional<Facility> findByFacilityId(Long facilityId);
}
