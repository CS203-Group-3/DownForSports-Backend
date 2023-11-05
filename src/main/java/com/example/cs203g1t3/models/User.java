package com.example.cs203g1t3.models;

import com.example.cs203g1t3.security.Otp.OneTimePassword;
import com.example.cs203g1t3.security.Otp.OneTimePasswordRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.*;
//import javax.validation.constraints.*;

import javax.validation.constraints.Email;


@Entity
@Table(name = "users", schema="public")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userID;

//    Login Details
    private String username;
    @NotBlank
//    @Size(min = 8, message = "Password should be at least 8 characters")
    private String password;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

//    //Other non-important identifiers
//     private String address;
     @Email
     private String email;
//     private Integer phoneNumber;
//     private boolean accountStatus;
//     private LocalDateTime lastActive;

//    //Variables to be used in the service later on
    private double creditScore;
//    private int noOfBookingsLeft;
//    private boolean isMember;

    //Email One Time Password
    @OneToOne(mappedBy="user",cascade=CascadeType.ALL,orphanRemoval = true)
    private OneTimePassword oneTimePassword;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Booking> bookings;


    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        creditScore = 600;
    }

    public Long getId() {
        return userID;
    }


    // implement method
//    public boolean makeBooking() {
//        return true;
//    }
//
//    // implement method
//    public List<Booking> listBooking() {
//        return null;
//    }
    
}
