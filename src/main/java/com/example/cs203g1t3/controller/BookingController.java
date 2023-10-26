package com.example.cs203g1t3.controller;

import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.cs203g1t3.repository.BookingRepository;
import com.example.cs203g1t3.repository.FacilityRepository;
import com.example.cs203g1t3.services.BookingService;
import com.example.cs203g1t3.services.FacilityService;

import jakarta.transaction.Transactional;

import com.example.cs203g1t3.exception.BookingNotFoundException;
import com.example.cs203g1t3.exception.FacilityNotFoundException;
import com.example.cs203g1t3.models.Booking;
import com.example.cs203g1t3.models.BookingRequest;
import com.example.cs203g1t3.models.Facility;

@CrossOrigin
@RestController
@RequestMapping("/api/facilities/{facilityId}/bookings")
public class BookingController {
    @Autowired
    private FacilityService facilityService;
    @Autowired
    private BookingService bookingService;

    
    // @GetMapping("/{bookingId}")
    // public ResponseEntity<Booking> getBookingById(@PathVariable Long facilityId, @PathVariable Long bookingId) {
    //     if (facilityService.getFacility(facilityId) == null) {
    //         throw new FacilityNotFoundException(facilityId);
    //     }
    //     Booking booking = bookingService.getBooking(bookingId);
    //     if (booking == null) {
    //         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    //     }
    //     return ResponseEntity.ok(booking);
    // }

    // @PutMapping("/{bookingId}")
    // public ResponseEntity<Booking> updateBooking(@PathVariable Long facilityId, @PathVariable Long bookingId, @RequestBody Booking updatedBooking) {
    //     if (facilityService.getFacility(facilityId) == null) {
    //         throw new FacilityNotFoundException(facilityId);
    //     }
    //     Booking booking = bookingService.updateBooking(facilityId,bookingId, updatedBooking);
    //     if (booking == null) {
    //         throw new BookingNotFoundException(bookingId);
    //     }
    //     return ResponseEntity.ok(booking);
    // }
    
    // @DeleteMapping("/{bookingId}")
    // public ResponseEntity<?> deleteBooking(@PathVariable Long facilityId, @PathVariable Long bookingId) {
    //     if (facilityService.getFacility(facilityId) == null) {
    //         throw new FacilityNotFoundException(facilityId);
    //     }
    //     try {   
    //         bookingService.deleteBooking(bookingId);
    //     } catch(EmptyResultDataAccessException e) {
    //         throw new BookingNotFoundException(bookingId);
    //     }
    //     return new ResponseEntity<>(HttpStatus.OK);
    // }
    
//-------------------------- Change all implementations below this line ---------------------------------------    

    // @GetMapping
    // public ResponseEntity<?> getAvailableTimeSlotsOfFacility(@PathVariable Long facilityId) {
    //     Facility facility = facilityService.getFacility(facilityId);
    //     if (facility == null) {
    //         throw new FacilityNotFoundException(facilityId);
    //     }
    //     return ResponseEntity.ok(facility.getTimeSlots());
    // }

    @PostMapping
    @Transactional
    public ResponseEntity<Booking> createBooking(@PathVariable Long facilityId, @Valid @RequestBody BookingRequest bookingRequest) {
        Facility facility = facilityService.getFacility(facilityId);
        if (facility == null) {
            throw new FacilityNotFoundException(facilityId);
        }
        
        bookingService.makeBooking(bookingRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
