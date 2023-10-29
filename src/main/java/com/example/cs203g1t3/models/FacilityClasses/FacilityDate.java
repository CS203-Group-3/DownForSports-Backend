package com.example.cs203g1t3.models.FacilityClasses;
import java.time.LocalDate;
import java.time.LocalTime;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.annotation.JsonIgnore;



@Entity
@Table(name = "facilityDate")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class FacilityDate {
    @Id
    @GeneratedValue (strategy =  GenerationType.IDENTITY)
    private Long facilityDateId;

    @ManyToOne
    @JoinColumn(name = "facility_id")
    @JsonIgnore
    private Facility facility;
    
    @OneToMany(mappedBy = "facilityDate", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<TimeSlots> timeSlots;
    private LocalDate date;

    @Autowired
    public FacilityDate(LocalDate date, List<TimeSlots> timeSlots) {
        this.date = date;
        this.timeSlots = timeSlots;
    }

}

