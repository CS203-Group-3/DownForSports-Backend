package com.example.cs203g1t3.servicesImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cs203g1t3.exception.InvalidCreditRequestException;
import com.example.cs203g1t3.models.Booking;
import com.example.cs203g1t3.models.CreditRequest;
import com.example.cs203g1t3.payload.request.CreditRequestform;
import com.example.cs203g1t3.repository.BookingRepository;
import com.example.cs203g1t3.repository.CreditRequestRepository;
import com.example.cs203g1t3.service.BookingService;
import com.example.cs203g1t3.service.CreditRequestService;
import com.example.cs203g1t3.service.UserService;

@Service
public class CreditRequestServiceImpl implements CreditRequestService {

    private BookingRepository bookingRepository;

    private CreditRequestRepository creditRequestRepository; 

    private UserService userService;

    public CreditRequestServiceImpl(BookingRepository bookingRepository, CreditRequestRepository creditRequestRepository, UserService userService) {
        this.bookingRepository = bookingRepository;
        this.creditRequestRepository = creditRequestRepository;
        this.userService = userService;
    }

    public void makeCreditRequest(CreditRequestform form) {
        Booking booking = bookingRepository.findByBookingId(form.getBookingId());
        if (form.getAmount() > booking.getCreditDeducted() || booking.isBookingAttended()) {
            throw new InvalidCreditRequestException();
        }
        creditRequestRepository.save(new CreditRequest(booking, form.getAmount(), form.getDetails()));
    }

    public void acceptRequest(int amount, long userId) {
        userService.addCreditScore(userId, amount);
    }

    public List<CreditRequest> getAllCreditRequest () {
        return creditRequestRepository.findAll();
    }
}