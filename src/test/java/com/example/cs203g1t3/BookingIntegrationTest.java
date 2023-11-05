package com.example.cs203g1t3;

import com.example.cs203g1t3.repository.BookingRepository;
import com.example.cs203g1t3.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookingIntegrationTest {
    @LocalServerPort
    private int port;

    private final String baseUrl = "http://localhost:";
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown(){
        bookingRepository.deleteALl();
        userRepository.deleteAll();
    }

    @Test
    public void getBooking_InvalidBookingId_Failure() throws Exception{
        URL uri = new URL(baseUrl + port + "/booking/")
    }

}
