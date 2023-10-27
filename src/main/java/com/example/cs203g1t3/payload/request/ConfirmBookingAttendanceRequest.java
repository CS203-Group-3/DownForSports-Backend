package com.example.cs203g1t3.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmBookingAttendanceRequest {
    @NotBlank
    private Long bookingId;

    //attendanceStatus = 1 => user attended booking, will return creditDeducted * 1.1 till 999
    //attendanceStatus = -1 => user did not attend booking, will NOT return creditDeducted
    //attendanceStatus = 0 => user exempted from being checked, will return creditDeducted
    @NotBlank
    private int attendanceStatus;

}
