package com.example.cs203g1t3.repository;

import com.example.cs203g1t3.models.Booking;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@Transactional
public interface BookingRepository extends JpaRepository<Booking,Long> {

    public List<Booking> findBookingsByUserId(Long userID);

    public Booking findByBookingId(Long bookingId);
    
}
