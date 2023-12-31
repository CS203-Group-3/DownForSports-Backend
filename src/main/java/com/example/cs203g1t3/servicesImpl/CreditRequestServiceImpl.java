package com.example.cs203g1t3.servicesImpl;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cs203g1t3.exception.InvalidCreditRequestException;
import com.example.cs203g1t3.models.Booking;
import com.example.cs203g1t3.models.CreditRequest;
import com.example.cs203g1t3.payload.request.CreditRequestform;
import com.example.cs203g1t3.payload.response.CreditRequestResponse;
import com.example.cs203g1t3.repository.BookingRepository;
import com.example.cs203g1t3.repository.CreditRequestRepository;
import com.example.cs203g1t3.service.BookingService;
import com.example.cs203g1t3.service.CreditRequestService;
import com.example.cs203g1t3.service.UserService;

import jakarta.transaction.Transactional;

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

    @Transactional
    public void acceptRequest(int amount, long userId, long creditRequestID) {
        try {
            userService.addCreditScore(userId, amount);
            creditRequestRepository.deleteByCreditRequestId(creditRequestID);
        } catch(Exception e) {
            throw new InvalidCreditRequestException();
        }
    }

    public List<CreditRequestResponse> getAllCreditRequest () {
        List<CreditRequest> cRequests =  creditRequestRepository.findAll();
        List<CreditRequestResponse> responses = cRequests.stream()
                                    .map(CreditRequest::toCreditRequestResponse)
                                    .collect(Collectors.toList());
        return responses;
    }
}