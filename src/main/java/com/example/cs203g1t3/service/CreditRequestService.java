package com.example.cs203g1t3.service;

import java.util.List;

import com.example.cs203g1t3.models.CreditRequest;
import com.example.cs203g1t3.payload.request.CreditRequestform;

public interface CreditRequestService {
    
    void makeCreditRequest(CreditRequestform form);

    void acceptRequest(int amount, long userId);

    List<CreditRequest> getAllCreditRequest ();

}
