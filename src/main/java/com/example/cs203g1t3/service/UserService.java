package com.example.cs203g1t3.service;

import com.example.cs203g1t3.models.Role;
import com.example.cs203g1t3.models.User;
import com.example.cs203g1t3.payload.request.SignupRequest;
import com.example.cs203g1t3.payload.response.MessageResponse;
import com.example.cs203g1t3.exception.NotEnoughCreditException;
import com.example.cs203g1t3.repository.UserRepository;
import com.example.cs203g1t3.security.jwt.JwtUtils;

import jakarta.mail.internet.MimeMessage;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.MessagingException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public interface UserService {

    List<User> getUsers(); 

    User getUser(Long userId);

    void deleteUser(Long userId);

    void changePassword(Long userID, String newPassword);

    boolean checkPassword(Long userID, String password);

    ResponseEntity<?> registerAccount(SignupRequest signUpRequest, Role role);

    void addCreditScore(Long userId,int creditScoreToAdd);

    void deductCredit(Long userId, double creditDeducted);
}


