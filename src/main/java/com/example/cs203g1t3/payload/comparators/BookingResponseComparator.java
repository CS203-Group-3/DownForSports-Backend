package com.example.cs203g1t3.payload.comparators;

import java.util.Comparator;

import com.example.cs203g1t3.payload.response.BookingResponse;

public class BookingResponseComparator implements Comparator<BookingResponse> {
    @Override
    public int compare(BookingResponse booking1, BookingResponse booking2) {
        // Compare dates first
        int dateComparison = booking1.getDate().compareTo(booking2.getDate());
        if (dateComparison != 0) {
            return dateComparison;
        }

        // If dates are equal, compare times
        return booking1.getStartTime().compareTo(booking2.getStartTime());
    }
}