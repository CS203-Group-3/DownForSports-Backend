package com.example.cs203g1t3.services;

import com.example.cs203g1t3.models.Booking;
import com.example.cs203g1t3.models.User;
import com.example.cs203g1t3.security.Email.EmailDetails;
import com.example.cs203g1t3.security.Email.EmailService;
import com.example.cs203g1t3.security.Email.EmailServiceImpl;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class NotificationServiceImpl {
    private final UserService userService;

    private final EmailService emailService;

    public NotificationServiceImpl(UserService userService, EmailServiceImpl emailService){
        this.userService = userService;
        this.emailService = emailService;
    }

    public void sendBookingConfirmationNotificationEmail(Long userId, Booking booking){
        User user = userService.getUser(userId);
        String userEmail = user.getEmail();
        LocalDate dateBooked = booking.getDateBooked();

        String messageBody = "Hello!\n\n This is a notification for you booking on " + dateBooked + " \n\n A total of " + booking.getCreditDeducted() + " credits have been deducted!\n\n\n Thank you very much!\n\nRegards,\nDownForSport";
        String messageSubject = "Notification : Booking Confirmation";
        EmailDetails emailDetails = new EmailDetails(userEmail,messageBody,messageSubject,null);//Email does not require any attachment
        emailService.sendSimpleMail(emailDetails);
    }


}
