package com.example.cs203g1t3.controller;

import com.example.cs203g1t3.exception.InvalidUserException;
import com.example.cs203g1t3.models.ERole;
import com.example.cs203g1t3.models.Role;
import com.example.cs203g1t3.models.User;
import com.example.cs203g1t3.payload.request.LoginRequest;
import com.example.cs203g1t3.payload.request.SignupRequest;
import com.example.cs203g1t3.payload.response.JwtResponse;
import com.example.cs203g1t3.payload.response.MessageResponse;
import com.example.cs203g1t3.payload.response.registrationResponse;
import com.example.cs203g1t3.repository.RoleRepository;
import com.example.cs203g1t3.repository.UserRepository;
import com.example.cs203g1t3.security.Otp.OneTimePasswordService;
import com.example.cs203g1t3.security.jwt.JwtUtils;
import com.example.cs203g1t3.service.UserService;
import com.example.cs203g1t3.servicesImpl.CustomUserDetails;
import com.example.cs203g1t3.servicesImpl.UserServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtUtils jwtUtils;

//    @Autowired
//    private RefreshTokenService refreshTokenService;

    @Autowired
    private OneTimePasswordService oneTimePasswordService;

    @GetMapping("/test")
    public String test(){
        return "Hello World";
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        if (!userRepository.existsByUsername(loginRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Account does not exist!"));
        }
        System.out.println("Authenticated");
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                        loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String jwt = jwtUtils.generateJwtToken(userDetails);

        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

//        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return ResponseEntity
                .ok(new JwtResponse(jwt,userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(),
                        roles));
    }

//    @PostMapping("/refreshtoken")
//    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
//        String requestRefreshToken = request.getRefreshToken();
//
//        return refreshTokenService.findByToken(requestRefreshToken)
//                .map(refreshTokenService::verifyExpiration)
//                .map(RefreshToken::getUser)
//                .map(user -> {
//                    String token = jwtUtils.generateTokenFromUsername(user.getUsername());
//                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
//                })
//                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
//                        "Refresh token is not in database!"));
//    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        Role userRole = roleRepository.findByName(ERole.ROLE_USER).get();
        User user;
        try {
            user = userService.registerAccount(signUpRequest, userRole);
            // oneTimePasswordService.generateOneTimePassword(user.getUserID());
        } catch (InvalidUserException e) {
            return ResponseEntity.badRequest().body(e);
        }
        return ResponseEntity.ok(new registrationResponse(new MessageResponse("User registered successfully!"), user.getUserID()));
    }

    @PostMapping("/registerBM")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createBookingManagerAcc(@Valid @RequestBody SignupRequest signUpRequest) {
        Role userRole = roleRepository.findByName(ERole.ROLE_BOOKINGMANAGER).get();
        try {userService.registerAccount(signUpRequest, userRole);}
        catch (InvalidUserException e) {
            return ResponseEntity.badRequest().body(e);
        }
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/deleteUser/{userId}")
    public ResponseEntity<?> deleteUser(@Valid @RequestParam Long userId){
        userService.deleteUser(userId);
        return ResponseEntity.ok(new MessageResponse("Please enter the OTP!"));
    }
    
}
