package com.example.cs203g1t3.models;

import jakarta.persistence.Table;

import com.example.cs203g1t3.payload.response.CreditRequestResponse;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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

    @OneToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    private int amount;
    private String requestDetails;

    public CreditRequest(Booking booking, int amount, String requestDetails) {
        this.booking = booking;
        this.amount = amount;
        this.requestDetails = requestDetails;
    }

    public CreditRequestResponse toCreditRequestResponse() {
        return new CreditRequestResponse(
            this.booking.getUser().getUsername(),
            this.booking.toBookingResponse(),
            this.amount,
            this.requestDetails
        );
    }
}
