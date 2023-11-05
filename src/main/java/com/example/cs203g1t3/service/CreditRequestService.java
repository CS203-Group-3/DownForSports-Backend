package com.example.cs203g1t3.service;

import java.util.List;

import com.example.cs203g1t3.models.CreditRequest;
import com.example.cs203g1t3.payload.request.CreditRequestform;
import com.example.cs203g1t3.payload.response.CreditRequestResponse;

public interface CreditRequestService {
    
    void makeCreditRequest(CreditRequestform form);

    void acceptRequest(int amount, long userId);

    List<CreditRequestResponse> getAllCreditRequest ();

}
