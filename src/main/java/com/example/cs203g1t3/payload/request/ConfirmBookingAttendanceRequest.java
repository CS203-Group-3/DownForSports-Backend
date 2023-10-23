package com.example.cs203g1t3.payload.request;

public class ConfirmBookingAttendanceRequest {

    private Long bookingId;

    //attendanceStatus = 1 => user attended booking, will return creditDeducted * 1.1 till 999
    //attendanceStatus = -1 => user did not attend booking, will NOT return creditDeducted
    //attendanceStatus = 0 => user exempted from being checked, will return creditDeducted
    private int attendanceStatus;

}
