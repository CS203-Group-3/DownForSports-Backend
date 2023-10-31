package com.example.cs203g1t3.services;

import com.example.cs203g1t3.models.Role;
import com.example.cs203g1t3.models.User;
import com.example.cs203g1t3.payload.request.SignupRequest;
import com.example.cs203g1t3.payload.response.MessageResponse;
import com.example.cs203g1t3.DTO.LoginResponse;
import com.example.cs203g1t3.exception.NotEnoughCreditException;
import com.example.cs203g1t3.repository.UserRepository;
import com.example.cs203g1t3.security.jwt.JwtUtils;

import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;

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

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    private BCryptPasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    public User getUser(Long userId) {
        // You can use your UserRepository or any data access method to fetch the user by userId
        Optional<User> user = userRepository.findById(userId);
        return user.get();
    }

    public void deleteUser(Long userId) {
        boolean exists = userRepository.existsById(userId);
        if (!exists) {
            throw new IllegalStateException("User with ID " + userId + "does not exists");
        }
        userRepository.deleteById(userId);
    }

    public void changePassword(Long userID, String newPassword) {
        // Encode the new password
        String encodedPassword = encoder.encode(newPassword);

        // Save the updated user with the new password
        userRepository.updatePassword(userID, encodedPassword);
    }

    public boolean checkPassword(Long userID, String password) {

        // Check if the id and password matches that of the database
        Optional<User> userOptional = userRepository.findByUserID(userID);

        String currentHash = userOptional.get().getPassword();
        // Return whether it exist
        return encoder.matches(password, currentHash);
    }

    public ResponseEntity<?> registerAccount(SignupRequest signUpRequest, Role role) {
        if (!isValidNric(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Please enter valid username!"));
        }
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }
        if (!isValidEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Please enter a valid email!"));
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }
        if (!isValidPassword(signUpRequest.getPassword())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Please enter a valid password!"));
        }
        User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<Role> roles = new HashSet<>();

        roles.add(role);
        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    public boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile("^(.+)@(gmail\\.com|yahoo\\.com)$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean isValidPassword(String password) {
        // Check if the password is at least 8 characters long
        if (password.length() < 8) {
            return false;
        }

        // Check for at least one uppercase letter
        Pattern uppercasePattern = Pattern.compile("[A-Z]");
        Matcher uppercaseMatcher = uppercasePattern.matcher(password);

        if (!uppercaseMatcher.find()) {
            return false;
        }

        // Check for at least one lowercase letter
        Pattern lowercasePattern = Pattern.compile("[a-z]");
        Matcher lowercaseMatcher = lowercasePattern.matcher(password);

        if (!lowercaseMatcher.find()) {
            return false;
        }

        // Check for at least one digit (number)
        Pattern digitPattern = Pattern.compile("[0-9]");
        Matcher digitMatcher = digitPattern.matcher(password);

        if (!digitMatcher.find()) {
            return false;
        }

        // Check for at least one special character
        Pattern specialCharacterPattern = Pattern.compile("[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]");
        Matcher specialCharacterMatcher = specialCharacterPattern.matcher(password);

        if (!specialCharacterMatcher.find()) {
            return false;
        }

        // If all checks pass, the password is valid
        return true;
    }

    public boolean isValidNric(String nric) {
        // Check if the NRIC is in the correct format
        Pattern pattern = Pattern.compile("^[STFG]\\d{7}[A-Z]$");
        Matcher matcher = pattern.matcher(nric);

        if (!matcher.matches()) {
            return false;
        }

        // Extract the first character (S/T/F/G)
        char firstChar = nric.charAt(0);

        // Extract the numeric part of the NRIC
        int numericPart = Integer.parseInt(nric.substring(1, 8));

        // Define the checksum letters for the first character
        char[] checksumLetters = "JZIHGFEDCBA".toCharArray();

        // Calculate the checksum based on the first character
        int checksum = (firstChar == 'T' || firstChar == 'G') ? 4 : 0;
        checksum += (numericPart % 10) * 2;

        // Check if the NRIC is valid by comparing the calculated checksum with the actual checksum letter
        return nric.charAt(8) == checksumLetters[checksum % 11];
    }

    public void addCreditScore(Long userId,int creditScoreToAdd){
        User user = userRepository.findById(userId).get();
        double currentCreditScore = user.getCreditScore();
        currentCreditScore += creditScoreToAdd;
        if(currentCreditScore >= 999){
            currentCreditScore = 999;
        }
        user.setCreditScore(currentCreditScore);
    }

        public void deductCredit(Long userId, double creditDeducted){
        User user = userRepository.findById(userId).orElse(null);
        if(user == null){
            return;
        }

        double currentCreditScore = user.getCreditScore();
        if(currentCreditScore < creditDeducted){
            throw new NotEnoughCreditException();
        }

        user.setCreditScore(currentCreditScore-creditDeducted);
        userRepository.save(user);
        }
}


