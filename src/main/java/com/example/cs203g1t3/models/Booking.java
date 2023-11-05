package com.example.cs203g1t3.models;

import java.util.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.example.cs203g1t3.models.FacilityClasses.Facility;
import com.example.cs203g1t3.payload.response.BookingResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "booking")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
    public class Booking {

        @Id
        @GeneratedValue (strategy =  GenerationType.IDENTITY)
        private Long bookingId;

        @ManyToOne
        @JoinColumn(name="userId")
        private User user;

        @Column(columnDefinition = "TIME")
        private LocalTime startTime;
        @Column(columnDefinition = "TIME")
        private LocalTime endTime;

        private LocalDateTime dateCreated;
        private LocalDate dateBooked;
        
        // private List<TimeSlots> timeSlots;
        private boolean bookingAttended;

        @ManyToOne
        @JsonIgnore
        @JoinColumn(name = "facility_id")
        private Facility facility;

        @Column(name = "bookingAttendanceChecked")
        private boolean bookingAttendanceChecked;

        @Column(name = "creditDeducted")
        private double creditDeducted;

        @OneToOne(mappedBy="booking", cascade = CascadeType.REMOVE,orphanRemoval = true)
        private CreditRequest creditRequest;

        public Booking(LocalTime startTime,LocalTime endTime,LocalDateTime dateCreated, double creditDeducted){
            this.startTime = startTime;
            this.endTime = endTime;
            this.dateCreated = dateCreated;
            this.creditDeducted = creditDeducted;
        }

        public boolean getBookingAttendanceChecked(){
            return bookingAttendanceChecked;
        }

        public BookingResponse toBookingResponse() {
            return new BookingResponse(
                this.bookingId,
                this.facility.getFacilityType(),
                this.facility.getDescription(),
                this.startTime.toString(),
                this.endTime.toString(),
                this.dateBooked.toString(),
                this.facility.getLocationString(),
                this.bookingAttendanceChecked,
                this.bookingAttended
            );
        }

//        public String getBooking

    }
