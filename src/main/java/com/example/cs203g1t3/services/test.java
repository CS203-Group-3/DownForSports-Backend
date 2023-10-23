package com.example.cs203g1t3.services;

import java.time.LocalTime;
import java.util.*;

import com.example.cs203g1t3.models.*;

public class test {
    public static void main(String[] args) {
        TimeSlots a = new TimeSlots(LocalTime.of(8,0), true);
        TimeSlots b = new TimeSlots(LocalTime.of(9,0), true);
            TimeSlots c = new TimeSlots(LocalTime.of(1,0), true);

        List<TimeSlots> l = new ArrayList<>();
        l.add(a);
        l.add(b);
        l.add(c);
        Collections.sort(l);
        // System.out.println(Collections.sort(l));
        System.out.println(l);

    }
}
