package com.example.cs203g1t3.servicesImpl;

import com.example.cs203g1t3.models.Booking;
import com.example.cs203g1t3.models.FacilityClasses.Facility;
import com.example.cs203g1t3.models.FacilityClasses.TimeSlots;
import com.example.cs203g1t3.models.User;
import com.example.cs203g1t3.security.Email.EmailDetails;
import com.example.cs203g1t3.security.Email.EmailService;
import com.example.cs203g1t3.security.Email.EmailServiceImpl;
import com.example.cs203g1t3.service.UserService;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional
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
        LocalTime startTime = booking.getStartTime();
        LocalTime endTime = booking.getEndTime();
        Long bookingId = booking.getBookingId();
        Facility facility = booking.getFacility();
        String facilityType = facility.getFacilityType();
        String facilityDesciption = facility.getDescription();
        String facilityLocation = facility.getLocationString();
        double creditDeducted = booking.getCreditDeducted();

        String messageBody = "Hello,\n\nYou have successfully made a booking on  + dateBooked" + " from " + startTime + " to " + endTime
                + ".\n\nHere are your booking details:\n\nBookingID: " + bookingId
                + "\nFacilityType: " + facilityType +
                "\nFacility Description: " + facilityDesciption
                + "\nFacility Location: " + facilityLocation
                + "\nCredit Deducted: " + creditDeducted
                + "\n\nHope you have fun at the facility! Please do take note to arrive 15 mins before your booked slot to confirm your attendance at the booking manager counter!\n\nIf you require any assistance, you can reach us at +65 XXXX XXXX.\n\n\nThank you!\nDownForSports Team";
        String messageSubject = "Notification : Booking Confirmation";
        EmailDetails emailDetails = new EmailDetails(userEmail,messageBody,messageSubject,null);
        emailService.sendSimpleMail(emailDetails);
    }

    public void sendBookingCancellationNotificationEmail(Long userId, Booking booking){
        User user = userService.getUser(userId);
        String userEmail = user.getEmail();
        LocalDate dateBooked = booking.getDateBooked();
        LocalTime startTime = booking.getStartTime();
        LocalTime endTime = booking.getEndTime();
        Long bookingId = booking.getBookingId();
        Facility facility = booking.getFacility();
        String facilityType = facility.getFacilityType();
        String facilityDesciption = facility.getDescription();
        String facilityLocation = facility.getLocationString();
        double creditDeducted = booking.getCreditDeducted();

        String messageBody = "Hello,\n\nYou have successfully cancelled your booking on "+ dateBooked + " from " + startTime + " to " + endTime
                + ".\n\nHere are your booking details:\n\nBookingID: " + bookingId
                + "\nFacilityType: " + facilityType +
                "\nFacility Description: " + facilityDesciption
                + "\nFacility Location: " + facilityLocation
                + "\nCredit Deducted: " + creditDeducted
                + "\n\nWe are sad to see you leave! Please come and book with us next time!\n\nIf you require any assistance, you can reach us at +65 XXXX XXXX.\n\n\nThank you!\nDownForSports Team";
        String messageSubject = "Notification : Booking Confirmation";
        EmailDetails emailDetails = new EmailDetails(userEmail,messageBody,messageSubject,null);
        emailService.sendSimpleMail(emailDetails);
    }

    public void sendBookingAttendedNotificationEmail(Long userId,Booking booking){
        User user = userService.getUser(userId);
        String userEmail = user.getEmail();
        LocalDate dateBooked = booking.getDateBooked();
        LocalTime startTime = booking.getStartTime();
        LocalTime endTime = booking.getEndTime();
        Long bookingId = booking.getBookingId();
        Facility facility = booking.getFacility();
        String facilityType = facility.getFacilityType();
        String facilityDesciption = facility.getDescription();
        String facilityLocation = facility.getLocationString();
        double creditDeducted = booking.getCreditDeducted();

        String messageBody = "Hello,\n\nYou have successfully confirmed your attendance for your booking on " + dateBooked + " from " + startTime + " to " + endTime
                + ".\n\nHere are your booking details:\n\nBookingID: " + bookingId
                + "\nFacilityType: " + facilityType +
                "\nFacility Description: " + facilityDesciption
                + "\nFacility Location: " + facilityLocation
                + "\nCredit Deducted: " + creditDeducted
                + "\n\nWe are sad to see you leave! Please come and book with us next time!\n\nIf you require any assistance, you can reach us at +65 XXXX XXXX.\n\n\nThank you!\nDownForSports Team";
        String messageSubject = "Notification : Booking Confirmation";
        EmailDetails emailDetails = new EmailDetails(userEmail,messageBody,messageSubject,null);
        emailService.sendSimpleMail(emailDetails);
    }


}
