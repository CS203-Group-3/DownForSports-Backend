 package com.example.cs203g1t3.services;

 import com.example.cs203g1t3.exception.BookingAttendanceIsDoneException;
 import com.example.cs203g1t3.exception.InvalidAttendanceStatusException;
 import com.example.cs203g1t3.exception.*;
 import com.example.cs203g1t3.models.*;
 import com.example.cs203g1t3.payload.request.*;
 import com.example.cs203g1t3.payload.response.ViewBookingsResponse;
 import com.example.cs203g1t3.repository.BookingRepository;

 import java.time.*;
 import java.util.*;

 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Service;

 @Service
 public class BookingService {
     @Autowired
     private BookingRepository bookingRepository;
     @Autowired
     private FacilityService facilityService;
     @Autowired
     private UserService userService;
    @Autowired
    private TimeSlotService timeSlotService;

    private final double attendancePresentMultiplier = 1.1;

     public BookingService(BookingRepository bookingRepository, FacilityService facilityService, UserService userService) {
         this.bookingRepository = bookingRepository;
         this.facilityService = facilityService;
         this.userService = userService;
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

    public boolean makeBooking(BookingRequest bookingRequest) {
        LocalDate dateBooked = bookingRequest.getFacilityDate();
        Long facilityId = bookingRequest.getFacilityId();
        Facility facility = facilityService.getFacility(facilityId);
        // if (facility == null) {
        //     // if facility not found throw
        //     throw new FacilityNotFoundException(facilityId);
        // }

        // get all timeslots from specific date
        List<TimeSlots> timeSlot = facilityService.getAllTimeSlotFromFacility(dateBooked, facilityId);
        if (timeSlot == null) {
            throw new TimeSlotNotFound();
        }
        

        List<LocalTime> bookingTimeSlot = bookingRequest.getTimeSlots();
        for (LocalTime t : bookingTimeSlot) {
            // check if it is available
            boolean isTaken = true;
            for (TimeSlots facilityTiming : timeSlot) {
                // if Bookedtimeslot is in facilitytimings, and is available, then break,
                if (facilityTiming.getStartTime().equals(t)
                        && facilityTiming.isAvailable()) {
                    isTaken = false;
                    break;
                }
            }
            // if timing is taken then throw exception
            if (isTaken) {
                throw new BookedException();
            }
        }

        //check if user has enough credits
        User user = userService.getUser(bookingRequest.getUserId());
        
        double credits = user.getCreditScore();
         //Amount of credit deducted = facility credit value * number of timeslots booked
        double creditDeducted = facility.getCreditCost() * bookingTimeSlot.size();
        // System.out.println(creditDeducted);
        // System.out.println(facility.getCreditCost());
        // System.out.println(credits);
        if(credits < creditDeducted){
            throw new NotEnoughCreditException();
        }
        Collections.sort(bookingTimeSlot);
        // System.out.println(bookingTimeSlot);
        LocalTime bookingStartTime = bookingTimeSlot.get(0);
        LocalTime bookingEndTime = bookingTimeSlot.get(bookingTimeSlot.size()-1).plusHours(1);
        // System.out.println(bookingStartTime);
        // System.out.println(bookingEndTime);

        userService.deductCredit(user.getUserID(), creditDeducted);
        // System.out.println(userService.getUser(bookingRequest.getUserId()));

        //make booking
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
        
        return true;
    }

    public List<ViewBookingsResponse> getUpcomingBookings(Long userId){
        List<Booking> usersBookings = bookingRepository.findBookingsByUserId(userId);
        List<ViewBookingsResponse> result = new ArrayList<>();
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
            result.add(new ViewBookingsResponse(i.getBookingId(),facility.getFacilityType(),facility.getDescription(),
                    i.getStartTime().toString(),i.getEndTime().toString(),i.getDateBooked().toString(),
                    facility.getLocationString()));
        }
        return result;
    }

    public List<ViewBookingsResponse> getPastBookings(Long userId){
        List<Booking> upcomingBookings = bookingRepository.findBookingsByUserId(userId);
        List<ViewBookingsResponse> result = new ArrayList<>();
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
            result.add(new ViewBookingsResponse(i.getBookingId(),facility.getFacilityType(),facility.getDescription(),
                    i.getStartTime().toString(),i.getEndTime().toString(),i.getDateBooked().toString(),
                    facility.getLocationString()));
        }
        return result;
    }

}

    // public Booking updateBooking(Long facilityId,Long bookingId, Booking
    // newBooking) {
    // //idea of this method
    // //get old booking -> remove bookingRepository from current booking list
    // //
    //
    // Booking booking = bookingRepository.findById(bookingId).orElse(null);
    // //scuffed code
    // if(booking == null){ //if booking not found
    // throw new BookingNotFoundException(bookingId);
    // }
    //
    // List<Booking> allBookings = getAllBookings();
    // allBookings.remove(booking); //remove current booking from the list
    //
    // List<LocalTime> timingTaken = new ArrayList<>(); //list of all timings that
    // is taken
    //
    // for(Booking tempBooking: allBookings){
    // List<LocalTime> tempList = listBookingTimings(tempBooking);
    // timingTaken.addAll(tempList);
    // }
    //
    // List<LocalTime> newBookingTiming = listBookingTimings(newBooking);
    //
    // // !Collections.disjoint(list1, list2) returns true if there is overlap
    // if(!Collections.disjoint(timingTaken, newBookingTiming)){
    // return null;
    // }
    //
    // Facility facility = facilities.findById(facilityId).orElse(null);
    //
    // List<LocalTime> facilityTimes = facility.getTimeSlots();
    // facilityTimes.addAll(listBookingTimings(booking));
    // facilityTimes.removeAll(newBookingTiming);
    // Collections.sort(facilityTimes);
    //
    // facility.setTimeSlots(facilityTimes);
    // facilities.save(facility);
    //
    // booking.setStartTime(newBooking.getStartTime());
    // booking.setEndTime(newBooking.getEndTime());
    // bookingRepository.save(booking);
    // return booking;
    //
    // //old impl
    // // return bookingRepository.findById(bookingId).map(booking -> {
    // // booking.setStartTime(newBooking.getStartTime());
    // // booking.setEndTime(newBooking.getEndTime());
    // // return bookingRepository.save(booking);
    // // }).orElse(null);
    // }

    // public boolean isValidBooking(LocalTime startTime, LocalTime endTime,
    // Facility facility) {
    // if(startTime.isAfter(endTime)){ //checking if startTime is after endTime
    // return false;
    // }
    // List<LocalTime> allAvailableTimings = facility.getTimeSlots();
    //
    // //check if each 30 minute slot starting from start time is
    // //available in the time available time slot in facility until the end time
    // while(startTime.isBefore(endTime)){
    // if(!allAvailableTimings.contains(startTime)){
    // return false;
    // }
    // startTime = startTime.plusMinutes(30);
    // }
    // return true;
    //
    // }


