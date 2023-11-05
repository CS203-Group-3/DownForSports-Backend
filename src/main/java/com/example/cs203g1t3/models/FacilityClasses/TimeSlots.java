package com.example.cs203g1t3.models.FacilityClasses;
import java.sql.Time;
import java.time.LocalTime;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class TimeSlots implements Comparable{
    @Id
    @GeneratedValue (strategy =  GenerationType.IDENTITY)
    private Long timeSlotsId;
    private LocalTime startTime;

    @JsonIgnore
    private boolean isAvailable;

    @ManyToOne
    @JoinColumn(name = "facilityDate_id")
    @JsonIgnore
    private FacilityDate facilityDate;

    @Autowired
    public TimeSlots(LocalTime startTime, boolean isAvailable) {
        this.startTime = startTime;
        this.isAvailable = isAvailable;
    }

    @Override
    public int compareTo(Object o) {
        TimeSlots temp = (TimeSlots)o;
        return startTime.compareTo(temp.getStartTime());
    }


    //c
    public boolean equals(TimeSlots other){
        return other.getTimeSlotsId() == timeSlotsId;
    }

    @Override
    public String toString() {
        return "TimeSlots [timeSlotsId=" + timeSlotsId + ", startTime=" + startTime + ", isAvailable=" + isAvailable
                +"]";
    }
    // The endTime is non-inclusive
    public boolean isBetweenTiming(LocalTime start,LocalTime end){
        if(!startTime.equals(start)){
            return startTime.isAfter(start) && startTime.isBefore(end);
        } else {
            return true;
        }
    }
}
