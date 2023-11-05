package com.example.cs203g1t3;

import com.example.cs203g1t3.models.User;
import com.example.cs203g1t3.repository.BookingRepository;
import com.example.cs203g1t3.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.net.URI;

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

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @AfterEach
    void tearDown(){
        bookingRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void getUpcomingBooking_Success() throws Exception{
        URI uri = new URI(baseUrl + port + "/booking");


    }

    @Test
    public void makeBooking_ValidBooking_Success() throws Exception{
        URI uri = new URI(baseUrl + port + "/booking");
    }

    @Test
    public void confirmBooking_Success() throws Exception{
        URI uri = new URI(baseUrl + port + "/booking");
    }
}
