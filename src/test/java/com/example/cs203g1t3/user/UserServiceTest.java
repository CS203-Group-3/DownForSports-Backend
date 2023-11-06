package com.example.cs203g1t3.user;

import com.example.cs203g1t3.exception.InvalidUserException;
import com.example.cs203g1t3.models.ERole;
import com.example.cs203g1t3.models.Role;
import com.example.cs203g1t3.models.User;
import com.example.cs203g1t3.payload.request.SignupRequest;
import com.example.cs203g1t3.repository.RoleRepository;
import com.example.cs203g1t3.repository.UserRepository;
import com.example.cs203g1t3.servicesImpl.UserServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private BCryptPasswordEncoder encoder;

    @InjectMocks
    private UserServiceImpl userService;


    @BeforeEach
    void mock_Repo() {
        roleRepository.save(new Role(ERole.ROLE_ADMIN));
        roleRepository.save(new Role(ERole.ROLE_BOOKINGMANAGER));
        roleRepository.save(new Role(ERole.ROLE_USER));

        userRepository.save(new User("S1325847C", "donta@gmail.com", "Testing@123"));
    }

    @Test
    void registerAccount_NewUser_ReturnedUser(){

        Set<Role> roles = new HashSet<>();
        Role role = new Role(ERole.ROLE_USER);
        roles.add(role);

        SignupRequest sUR = new SignupRequest("T0127510G","junjie.liew2001@gmail.com", "Ilovecs203!");
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(sUR.getPassword());
        when(encoder.encode(any(CharSequence.class))).thenReturn(encodedPassword);

        sUR.setPassword(encodedPassword);
        User user = new User(sUR.getUsername(), sUR.getEmail(), sUR.getPassword());
        user.setRoles(roles);

        User savedUser = userService.registerAccount(sUR, role);

        assertNotNull(savedUser);
        verify(userRepository).save(user);
        
    }

    @Test
    void registerAccount_SameUserName_ThrowsException() {

        SignupRequest sUR = new SignupRequest("S1325847C", "someemail@example.com", "SomePassword");
        when(userRepository.existsByUsername(sUR.getUsername())).thenReturn(true);
        Role role = new Role(ERole.ROLE_USER);

        Exception exception = assertThrows(InvalidUserException.class, () -> {
            userService.registerAccount(sUR, role);
        });

        assertEquals("Error: Username is already taken!", exception.getMessage());
    }

    @Test
    void registerAccount_InvalidNRIC_ThrowsException() {

        SignupRequest sUR = new SignupRequest("S1234567A", "someemail@example.com", "SomePassword");
        Role role = new Role(ERole.ROLE_USER);

        Exception exception = assertThrows(InvalidUserException.class, () -> {
            userService.registerAccount(sUR, role);
        });

        assertEquals("Error: Please enter valid username!", exception.getMessage());
    }

    @Test
    void changePassword_Successful_ReturnedNewPass() {
        long i = 1;
        String oldPass = "Testing@123";
        User user = new User("S1325847C", "donta@gmail.com", "Testing@123");
        
        // Mock the userRepository.findByUserID method to return an Optional containing the user
        when(userRepository.findByUserID(i)).thenReturn(Optional.of(user));
        
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(oldPass);
        
        // Mock the encoder.encode method
        when(encoder.encode("Pass1234!")).thenReturn(encodedPassword);

        Boolean correctPass = userService.checkPassword(i, oldPass);
        String newPass = userService.changePassword(i, "Pass1234!");

        assertEquals(newPass, "Pass1234!");
        verify(userRepository).findByUserID(i);
        verify(userRepository).updatePassword(i, encodedPassword);
    }

    @Test
    void changePassword_WrongPass_ReturnedFalse() {
        long i = 2;
        String wrongPass = "WrongPassword";  // Provide a wrong password
        User user = new User("S1325847C", "donta@gmail.com", "Testing@123");
        
        // Mock the userRepository.findByUserID method to return an Optional containing the user
        when(userRepository.findByUserID(i)).thenReturn(Optional.of(user));
        
        // Call the service method to check if the wrong password is correct
        Boolean correctPass = userService.checkPassword(i, wrongPass);
        
        // Check that correctPass is false, indicating the wrong password is not correct
        assertFalse(correctPass);
    }


    // @Test
    // void logout_ValidUser_DeleteByUserCalled() {
    //     // Arrange
    //     Long userId = 2L;
    //     User user = new User("S1325847C", "donta@gmail.com", "Testing@123");
    //     // Mock the behavior of userRepository to return a User when findById is called
    //     when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    //     // Mock the behavior of refreshTokenRepository to return 1 when deleteByUser is called
    //     when(refreshTokenRepository.deleteByUser(any(User.class))).thenReturn(1);


    //     // Act
    //     userService.logUserOut(userId);

    //     // Assert
    //     verify(refreshTokenRepository).deleteByUser(any());
    // }



}
