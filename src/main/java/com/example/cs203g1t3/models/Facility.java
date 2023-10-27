package com.example.cs203g1t3.models;

import java.time.LocalTime;
import java.util.List;

import org.springframework.cglib.core.Local;

// import com.example.cs203g1t3.payload.response.FacilityAvailablityResponse;
import com.example.cs203g1t3.payload.response.FacilityResponse;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "facility")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode

// LocalDateTime reference : 
    // https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/time/package-summary.html


public class Facility {

    @Id
    @GeneratedValue (strategy =  GenerationType.IDENTITY)
    private Long facilityId;
    private String facilityType;
    private String description;
    private LocalTime openTime;
    private LocalTime closingTime;
    private int creditCost;
    private String locationString;

    @OneToMany(mappedBy = "facility", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<FacilityDate> facilityDates;

    @OneToMany(mappedBy = "facility", cascade = CascadeType.ALL) // putting orphanRemoval = true gives the error "A collection with cascade="all-delete-orphan" was no longer referenced by the owning entity instance"
    private List<Booking> bookings;

    public Facility(String facilityType, String description) {
        this.facilityType = facilityType;
        this.description = description;
    }

    public Facility(String facilityType, String description, LocalTime openTime, LocalTime closingTime, int creditCost, String locationString) {
        this.facilityType = facilityType;
        this.description = description;
        this.openTime = openTime;
        this.closingTime = closingTime;
        this.creditCost = creditCost;
        this.locationString = locationString;
    }

    public FacilityResponse toFacilityResponse() {
        return new FacilityResponse(
            this.facilityId,
            this.facilityType,
            this.description,
            this.openTime,
            this.closingTime,
            this.creditCost,
            this.locationString
        );
    }


}
