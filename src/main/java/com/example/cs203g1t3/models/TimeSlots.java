package com.example.cs203g1t3.models;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "timeslots")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class TimeSlots {
    @Id
    @GeneratedValue (strategy =  GenerationType.IDENTITY)
    private Long timeSlotsId;
    private LocalTime startTime;
    private boolean isAvailable;

    @ManyToOne
    @JoinColumn(name = "facilityDate_id")
    private FacilityDate facilityDate;

    @Autowired
    public TimeSlots(LocalTime startTime, boolean isAvailable) {
        this.startTime = startTime;
        this.isAvailable = isAvailable;
    }

    

}
