package com.example.cs203g1t3.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "credit_request")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CreditRequest {
    @Id
    @GeneratedValue (strategy =  GenerationType.IDENTITY)
    private Long creditID;

    @OneToOne(cascade = CascadeType.REMOVE,orphanRemoval = true)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    private int amount;
    private String requestDetails;

    public CreditRequest(Booking booking, int amount, String requestDetails) {
        this.booking = booking;
        this.amount = amount;
        this.requestDetails = requestDetails;
    }
}
