package com.example.cs203g1t3.services;

import com.example.cs203g1t3.models.Booking;
import com.example.cs203g1t3.models.Facility;
import com.example.cs203g1t3.repository.FacilityRepository;
import java.util.*;

import org.springframework.stereotype.Service;

@Service
public class FacilityService {
    private FacilityRepository facilities;

    public FacilityService(FacilityRepository facilities) {
        this.facilities = facilities;
    }



    public List<Facility> listFacilities() {
        return facilities.findAll();
    }

    public Facility getFacility(Long facilityId) {
        return facilities.findById(facilityId).orElse(null);
    }

    public List<Facility> getAllFacilities() {
        return facilities.findAll();
    }

    public Facility createFacility(Facility facility) {
        return facilities.save(facility);
    }


    public Facility updateFacility(Long facilityId, Facility newFacility) {
        return facilities.findById(facilityId).map(facility -> {
                facility.setFacilityType(newFacility.getFacilityType());
                facility.setDescription(newFacility.getDescription());
                facility.setOpenTime(newFacility.getOpenTime());
                facility.setClosingTime(newFacility.getClosingTime());
                return facilities.save(facility);
            }).orElse(null);
    }

    public void deleteFacility(Long facilityId) {
        facilities.deleteById(facilityId);
    }


}
