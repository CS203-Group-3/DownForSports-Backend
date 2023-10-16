package com.example.cs203g1t3.controller;

import com.example.cs203g1t3.models.User;
import com.example.cs203g1t3.services.UserService;
import com.example.cs203g1t3.payload.request.ChangePasswordRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/user")
public class UserController {

    public UserService userService;

//    private BCryptPasswordEncoder encoder;

    @Autowired
    public UserController(UserService us) {
        this.userService = us;
    }

    // Testing to get users -> Send a GET request here to check it's working.
    // Or just use PostgresSQL

    @GetMapping("/details/{userId}")
    public ResponseEntity<?> getUserProfile(@PathVariable Long userId) {
        User user = userService.getUser(userId);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<?> changePassword(@PathVariable Long userId, @RequestBody ChangePasswordRequest changePasswordRequest) {
        // Assuming ChangePasswordRequest is a class that holds new password and other necessary data
        String newPassword = changePasswordRequest.getNewPassword();
        String currentPassword = changePasswordRequest.getCurrentPassword();

        // Implement authentication logic to ensure the user is authorized
        User user = userService.getUser(userId);
        
        // Check if the current password provided matches the user's current password
        if (userService.checkPassword(user, currentPassword)) {
            return ResponseEntity.badRequest().body("Current password is incorrect");
        }

        // Perform validation on the new password, e.g., length, complexity, etc.
        // If validation fails, return an error response.

        // Update the user's password in the database
        userService.changePassword(user, newPassword);

        return ResponseEntity.ok("Password changed successfully");
    }
}

