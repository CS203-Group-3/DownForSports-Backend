package com.example.cs203g1t3.servicesImpl;

import com.example.cs203g1t3.models.ERole;
import com.example.cs203g1t3.models.Role;
import com.example.cs203g1t3.models.User;
import com.example.cs203g1t3.payload.request.SignupRequest;
import com.example.cs203g1t3.payload.response.MessageResponse;
import com.example.cs203g1t3.exception.NotEnoughCreditException;
import com.example.cs203g1t3.repository.UserRepository;
import com.example.cs203g1t3.security.jwt.JwtUtils;
import com.example.cs203g1t3.service.UserService;
import com.example.cs203g1t3.exception.InvalidUserException;

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
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Autowired
    private final RefreshTokenService refreshTokenService;

    private BCryptPasswordEncoder encoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder encoder,RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.refreshTokenService = refreshTokenService;
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
         userRepository.deleteByUserID(userId);
     }

    public String changePassword(Long userID, String newPassword) {
        // Encode the new password
        String encodedPassword = encoder.encode(newPassword);

        // Save the updated user with the new password
        userRepository.updatePassword(userID, encodedPassword);
        return newPassword;
    }

    public void logUserOut(Long userId){
        refreshTokenService.deleteByUserId(userId);
    }

    public boolean checkPassword(Long userID, String password) {

        // Check if the id and password matches that of the database
        Optional<User> userOptional = userRepository.findByUserID(userID);

        String currentHash = userOptional.get().getPassword();
        // Return whether it exist
        return encoder.matches(password, currentHash);
    }

    public User registerAccount(SignupRequest signUpRequest, Role role) {

        if (!isValidNric(signUpRequest.getUsername()) && !(role.getERole() == ERole.ROLE_BOOKINGMANAGER) ) {
            throw new InvalidUserException("Error: Please enter valid username!");
        }
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new InvalidUserException("Error: Username is already taken!");
        }
        if (!isValidEmail(signUpRequest.getEmail())) {
            throw new InvalidUserException("Error: Please enter a valid email!");
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new InvalidUserException("Error: Email is already in use!");
        }
        if (!isValidPassword(signUpRequest.getPassword())) {
            throw new InvalidUserException("Error: Please enter a valid password!");
        }
        User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<Role> roles = new HashSet<>();

        roles.add(role);
        user.setRoles(roles);
        userRepository.save(user);

        return user;
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

    // Check if NRIC is valid
    public static boolean isValidNric(String inputString) {
        String nricToTest = inputString.toUpperCase();
    
        // first letter must start with S, T, F or G. Last letter must be A - Z
        if (!Pattern.compile("^[STFG]\\d{7}[A-Z]$").matcher(nricToTest).matches()) {
            return false;
        } else {
            char[] icArray = new char[9];
            char[] st = "JZIHGFEDCBA".toCharArray();
            char[] fg = "XWUTRQPNMLK".toCharArray();
    
            for (int i = 0; i < 9; i++) {
                icArray[i] = nricToTest.charAt(i);
            }
    
            // calculate weight of positions 1 to 7
            int weight = (Integer.parseInt(String.valueOf(icArray[1]), 10)) * 2 + 
                    (Integer.parseInt(String.valueOf(icArray[2]), 10)) * 7 +
                    (Integer.parseInt(String.valueOf(icArray[3]), 10)) * 6 +
                    (Integer.parseInt(String.valueOf(icArray[4]), 10)) * 5 +
                    (Integer.parseInt(String.valueOf(icArray[5]), 10)) * 4 +
                    (Integer.parseInt(String.valueOf(icArray[6]), 10)) * 3 +
                    (Integer.parseInt(String.valueOf(icArray[7]), 10)) * 2;
    
            int offset = icArray[0] == 'T' || icArray[0] == 'G' ? 4 : 0;
    
            int lastCharPosition = (offset + weight) % 11;
    
            if (icArray[0] == 'S' || icArray[0] == 'T') {
                return icArray[8] == st[lastCharPosition];
            } else if (icArray[0] == 'F' || icArray[0] == 'G') {
                return icArray[8] == fg[lastCharPosition];
            } else {
                return false; // this line should never reached due to regex above
            }
        }
    }

    public void addCreditScore(Long userId, double creditScoreToAdd){
        User user = userRepository.findById(userId).get();
        double currentCreditScore = user.getCreditScore();
        currentCreditScore += creditScoreToAdd;
        if(currentCreditScore >= 999){
            currentCreditScore = 999;
        }
        user.setCreditScore(currentCreditScore);
        userRepository.updateCreditScore(userId, currentCreditScore);
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


