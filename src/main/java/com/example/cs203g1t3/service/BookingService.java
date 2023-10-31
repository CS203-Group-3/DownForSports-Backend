 package com.example.cs203g1t3.service;

import java.util.List;

import com.example.cs203g1t3.models.Booking;
import com.example.cs203g1t3.payload.request.BookingRequest;
import com.example.cs203g1t3.payload.request.CancelBookingRequest;
import com.example.cs203g1t3.payload.response.ViewPastBookingsResponse;
import com.example.cs203g1t3.payload.response.ViewUpcomingBookingsResponse;


public interface BookingService {

    // Returns booking of that bookingId
    Booking getBooking(Long bookingId); 

    // Returns list of all bookings
    List<Booking> getAllBookings(); 

    // Cancels a booking
    void cancelBooking(CancelBookingRequest cancelBookingRequest);

    // For booking managers to confirm if a booking is attended
    void confirmBookingAttendance(Long bookingId,int attendanceStatus);

    // Making a booking
    boolean makeBooking(BookingRequest bookingRequest);

    // Returns bookings that date has yet to pass today
    List<ViewUpcomingBookingsResponse> getUpcomingBookings(Long userId);

    // Returns bookings that date has pass today
    List<ViewPastBookingsResponse> getPastBookings(Long userId);

}

