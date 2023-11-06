package com.example.cs203g1t3.service;

import com.example.cs203g1t3.models.Role;
import com.example.cs203g1t3.models.User;
import com.example.cs203g1t3.payload.request.SignupRequest;
import java.util.List;


public interface UserService {

    List<User> getUsers(); 

    User getUser(Long userId);

    // void deleteUser(Long userId);

    String changePassword(Long userID, String newPassword);

    boolean checkPassword(Long userID, String password);

    User registerAccount(SignupRequest signUpRequest, Role role);

    void addCreditScore(Long userId,double creditScoreToAdd);

    void deductCredit(Long userId, double creditDeducted);

    void deleteUser(Long userId);
}


