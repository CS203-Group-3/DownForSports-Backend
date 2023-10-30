package com.example.cs203g1t3.controller;

import java.util.List;

import javax.validation.Valid;

import com.example.cs203g1t3.payload.request.CancelBookingRequest;
import com.example.cs203g1t3.payload.request.ConfirmBookingAttendanceRequest;
import com.example.cs203g1t3.payload.response.ViewBookingsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.cs203g1t3.services.BookingService;
import com.example.cs203g1t3.services.FacilityService;

import jakarta.transaction.Transactional;

import com.example.cs203g1t3.exception.FacilityNotFoundException;
import com.example.cs203g1t3.models.Booking;
import com.example.cs203g1t3.models.Facility;
import com.example.cs203g1t3.payload.request.BookingRequest;

@CrossOrigin
@RestController
@RequestMapping("/api/bookings")
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

    @PostMapping("/makebooking")
    @Transactional
    public ResponseEntity<Booking> createBooking(@Valid @RequestBody BookingRequest bookingRequest) {
        Long facilityId = bookingRequest.getFacilityId();
        Facility facility = facilityService.getFacility(facilityId);
        if (facility == null) {
            throw new FacilityNotFoundException(facilityId);
        }

        bookingService.makeBooking(bookingRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/confirmbookingattendance")
    @Transactional
//    @PreAuthorize("hasRole('ROLE_BOOKINGMANAGER')")
    public ResponseEntity<Booking> confirmBookingAttendance(@RequestBody ConfirmBookingAttendanceRequest confirmBookingAttendanceRequest) {
        Long bookingId = confirmBookingAttendanceRequest.getBookingId();
        int attendanceStatus = confirmBookingAttendanceRequest.getAttendanceStatus();
        bookingService.confirmBookingAttendance(bookingId, attendanceStatus);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/cancelbooking")
    public ResponseEntity<?> cancelBooking(@RequestBody CancelBookingRequest cancelBookingRequest){
        bookingService.cancelBooking(cancelBookingRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/viewupcomingbookings")
    public List<ViewBookingsResponse> getUpcomingBookings(@RequestParam Long userId) {
        return bookingService.getUpcomingBookings(userId);
    }

    @GetMapping("/viewpastbookings")
    public List<ViewBookingsResponse> getPastBookings(@RequestParam Long userId) {
        return bookingService.getPastBookings(userId);
    }


}

