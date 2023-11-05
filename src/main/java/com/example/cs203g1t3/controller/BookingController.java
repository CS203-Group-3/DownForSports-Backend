package com.example.cs203g1t3.controller;

import java.util.List;

import javax.validation.Valid;

import com.example.cs203g1t3.payload.request.CancelBookingRequest;
import com.example.cs203g1t3.payload.request.ConfirmBookingAttendanceRequest;
import com.example.cs203g1t3.payload.request.CreditRequestform;
import com.example.cs203g1t3.payload.request.ViewBookingsRequest;
import com.example.cs203g1t3.payload.response.BookingResponse;
import com.example.cs203g1t3.payload.response.CreditRequestResponse;
import com.example.cs203g1t3.payload.response.MessageResponse;
import com.example.cs203g1t3.payload.response.ViewPastBookingsResponse;
import com.example.cs203g1t3.payload.response.ViewUpcomingBookingsResponse;
import com.example.cs203g1t3.service.BookingService;
import com.example.cs203g1t3.service.CreditRequestService;
import com.example.cs203g1t3.service.FacilityService;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.transaction.Transactional;

import com.example.cs203g1t3.exception.FacilityNotFoundException;
import com.example.cs203g1t3.models.Booking;
import com.example.cs203g1t3.models.CreditRequest;
import com.example.cs203g1t3.models.FacilityClasses.Facility;
import com.example.cs203g1t3.payload.comparators.BookingResponseComparator;
import com.example.cs203g1t3.payload.request.BookingRequest;

@CrossOrigin
@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    @Autowired
    private FacilityService facilityService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private CreditRequestService creditRequestService;

    @GetMapping("/")
    public List<BookingResponse> getAllBookingForBookingManager() {
        List<BookingResponse> bookingResponses = bookingService.getAllBookingNotChecked();
    
        Collections.sort(bookingResponses, new BookingResponseComparator());

        return bookingResponses;
    }
    

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

    // @GetMapping("/viewupcomingbookings")
    // public List<ViewUpcomingBookingsResponse> getUpcomingBookings(@RequestBody ViewBookingsRequest viewBookingsRequest){
    //     return bookingService.getUpcomingBookings(viewBookingsRequest.getUserId());
    // }
    @GetMapping("/viewupcomingbookings")
    public List<BookingResponse> getUpcomingBookings(@RequestParam Long userId) {
        return bookingService.getUpcomingBookings(userId);
    }


    // @GetMapping("/viewpastbookings")
    // public List<ViewPastBookingsResponse> getPastBookings(@RequestBody ViewBookingsRequest viewBookingsRequest){
    //     return bookingService.getPastBookings(viewBookingsRequest.getUserId());
    // }
    @GetMapping("/viewpastbookings")
    public List<BookingResponse> getPastBookings(@RequestParam Long userId) {
        return bookingService.getPastBookings(userId);
    }


    @PostMapping("/creditrequest")
    public ResponseEntity<?> createCreditRequest(@RequestBody CreditRequestform form) {
        try {
            creditRequestService.makeCreditRequest(form); 
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Request amount cannot be more then cost!"));
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_BOOKINGMANAGER')")
    @GetMapping("/creditrequest")
    public ResponseEntity<List<CreditRequestResponse>> getCreditReqeust() {
        return ResponseEntity.ok(creditRequestService.getAllCreditRequest());
    }

    @PreAuthorize("hasRole('ROLE_BOOKINGMANAGER')")
    @PostMapping("/creditrequest/confirm")
    public ResponseEntity<?> confirmCreditRequest(@RequestBody long userID ,@RequestBody int refundAmount) {
        creditRequestService.acceptRequest(refundAmount, userID);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

