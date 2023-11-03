 package com.example.cs203g1t3.services;

 import com.example.cs203g1t3.exception.*;
 import com.example.cs203g1t3.models.*;
 import com.example.cs203g1t3.payload.request.*;
 import com.example.cs203g1t3.payload.response.ViewPastBookingsResponse;
 import com.example.cs203g1t3.payload.response.ViewUpcomingBookingsResponse;
 import com.example.cs203g1t3.repository.BookingRepository;

import jakarta.transaction.Transactional;

import java.time.*;
 import java.util.*;

 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Service;

 @Service
 @Transactional
 public class BookingService {
     @Autowired
     private BookingRepository bookingRepository;
     @Autowired
     private FacilityService facilityService;
     @Autowired
     private UserService userService;
    @Autowired
    private TimeSlotService timeSlotService;

    private NotificationServiceImpl notificationService;

    private final double attendancePresentMultiplier = 1.1;

     public BookingService(BookingRepository bookingRepository, FacilityService facilityService, UserService userService,NotificationServiceImpl notificationService) {
         this.bookingRepository = bookingRepository;
         this.facilityService = facilityService;
         this.userService = userService;
         this.notificationService = notificationService;
     }

     public Booking getBooking(Long bookingId) {
         return bookingRepository.findById(bookingId).orElse(null);
     }

     public List<Booking> getAllBookings() {
         return bookingRepository.findAll();
     }

     public void cancelBooking(CancelBookingRequest cancelBookingRequest) {
         Long bookingId = cancelBookingRequest.getBookingId();
         Booking booking;
         try {
             booking = bookingRepository.findById(bookingId).get();
         } catch (NoSuchElementException e) {
             throw new BookingNotFoundException("Booking is not found! Unable to cancel!");
         }
         if (booking.getBookingAttendanceChecked()) {
             throw new BookingAttendanceIsDoneException("Booking has already been attended!");
         }
         LocalDate dateBooked = booking.getDateBooked();
         Long facilityId = booking.getFacility().getFacilityId();
         List<TimeSlots> timeSlotsList = facilityService.getAllTimeSlotFromFacility(dateBooked, facilityId);
         for (TimeSlots i : timeSlotsList) {
             if (i.isBetweenTiming(booking.getStartTime(),booking.getEndTime())) {
                 timeSlotService.updateToAvailable(i.getTimeSlotsId());

             }
             bookingRepository.deleteById(bookingId);
         }
     }

     public void confirmBookingAttendance(Long bookingId,int attendanceStatus){
         if(attendanceStatus != 1 && attendanceStatus != 0 && attendanceStatus != -1){
             throw new InvalidAttendanceStatusException("Invalid Attendance Status Code");
         }
         Booking booking = bookingRepository.findById(bookingId).get();
         if(booking.getBookingAttendanceChecked()){
             throw new BookingAttendanceIsDoneException("Booking Attendance Is Already Updated!");
         }
         User user = booking.getUser();
         double currentCreditScore = user.getCreditScore();

        switch(attendanceStatus){
            case 1:
                user.setCreditScore(currentCreditScore + booking.getCreditDeducted() * attendancePresentMultiplier);
                booking.setBookingAttended(true);
                break;
            case -1:
                user.setCreditScore(currentCreditScore+booking.getCreditDeducted());
                booking.setBookingAttended(false);
                break;
        }
        booking.setBookingAttendanceChecked(true);
     }

     @Transactional
    public boolean makeBooking(BookingRequest bookingRequest) {
        LocalDate dateBooked = bookingRequest.getFacilityDate();
        Long facilityId = bookingRequest.getFacilityId();
        Facility facility = facilityService.getFacility(facilityId);
        //check if timeslots are consecutive
        if(!isConsecutiveTimeSlots(bookingRequest.getTimeSlots())){
            throw new BookedException("TimeSlots are not consecutive");
        }
        // get all timeslots from specific date
        List<TimeSlots> timeSlot = facilityService.getAllTimeSlotFromFacility(dateBooked, facilityId);
        if (timeSlot == null) {
            throw new TimeSlotNotFound();
        }

        List<LocalTime> bookingTimeSlot = bookingRequest.getTimeSlots();
        //if timeslot booked is not available, throw exception
        if(!checkAvailability(bookingTimeSlot, timeSlot)){
            throw new BookedException("Booking timeslots are taken");
        }

        //check if user has enough credits
        User user = userService.getUser(bookingRequest.getUserId());
        
        double credits = user.getCreditScore();
         //Amount of credit deducted = facility credit value * number of timeslots booked
        double creditDeducted = facility.getCreditCost() * bookingTimeSlot.size();

        if(credits < creditDeducted){
            throw new NotEnoughCreditException();
        }
        Collections.sort(bookingTimeSlot);

        LocalTime bookingStartTime = bookingTimeSlot.get(0);
        LocalTime bookingEndTime = bookingTimeSlot.get(bookingTimeSlot.size()-1).plusHours(1);
        userService.deductCredit(user.getUserID(), creditDeducted);

        //make booking onject
        Booking booking = new Booking(bookingStartTime,bookingEndTime,LocalDateTime.now(),creditDeducted);
        booking.setFacility(facility);
        booking.setDateBooked(dateBooked);
        booking.setUser(user);
        bookingRepository.save(booking);

        //set timings isavailable to false
        for(LocalTime time:bookingTimeSlot){
            //cross ref List<timeslots> to get timeslotId
            for(TimeSlots t:timeSlot){
                if(time.equals(t.getStartTime())){
                    timeSlotService.updateToUnavailable(t.getTimeSlotsId());
                }
            }
        }
        notificationService.sendBookingConfirmationNotificationEmail(user.getUserID(),booking);
        return true;
    }

    public boolean checkAvailability(List<LocalTime>bookingTime, List<TimeSlots> facilityTimings){
        for (LocalTime t : bookingTime) {
            // check if it is available
            boolean isTaken = true;
            for (TimeSlots facilityTiming : facilityTimings) {
                // if Bookedtimeslot is in facilitytimings, and is available, then break,
                if (facilityTiming.getStartTime().equals(t)
                        && facilityTiming.isAvailable()) {
                    isTaken = false;
                    break;
                }
            }
            // if timing is taken then return false
            if (isTaken) {
                return false;
            }
        }
        return true;
    }
    public boolean isConsecutiveTimeSlots(List<LocalTime> timeslots){
        Collections.sort(timeslots);
        LocalTime start = timeslots.get(0);
        for(LocalTime time: timeslots){
            if(!time.equals(start)){
                return false;
            }
            start = start.plusHours(1);
        }
        return true;
    }
    public List<ViewUpcomingBookingsResponse> getUpcomingBookings(Long userId){
        List<Booking> usersBookings = bookingRepository.findBookingsByUserId(userId);
        List<ViewUpcomingBookingsResponse> result = new ArrayList<>();
        LocalDate todayDate = LocalDate.now();
        LocalTime timeNow = LocalTime.now();
        for(Booking i : usersBookings){
            if(i.getDateBooked().equals(todayDate)){
                if(timeNow.isAfter(i.getStartTime())){
                    continue;
                }
                if(timeNow.equals(i.getStartTime())){
                    continue;
                }
            } else if(i.getDateBooked().isBefore(todayDate)){
                continue;
            }
            Facility facility = i.getFacility();
            result.add(new ViewUpcomingBookingsResponse(facility.getFacilityType(),facility.getDescription(),
                    i.getStartTime().toString(),i.getEndTime().toString(),i.getDateBooked().toString(),
                    facility.getLocationString()));
        }
        return result;
    }

    public List<ViewPastBookingsResponse> getPastBookings(Long userId){
        List<Booking> upcomingBookings = bookingRepository.findBookingsByUserId(userId);
        List<ViewPastBookingsResponse> result = new ArrayList<>();
        LocalDate todaysDate = LocalDate.now();
        LocalTime timeNow = LocalTime.now();
        for(Booking i : upcomingBookings){
            if(i.getDateBooked().equals(todaysDate)){
                if(timeNow.isBefore(i.getStartTime())){
                    continue;
                }
            } else if(i.getDateBooked().isAfter(todaysDate)){
                continue;
            }
            Facility facility = i.getFacility();
            result.add(new ViewPastBookingsResponse(facility.getFacilityType(),facility.getDescription(),
                    i.getStartTime().toString(),i.getEndTime().toString(),i.getDateBooked().toString(),
                    facility.getLocationString()));
        }
        return result;
    }

    public List<Booking> getBookingsFromFacilityId(Long facilityId){
         return bookingRepository.findBookingByFacilityId(facilityId);
    }

}
