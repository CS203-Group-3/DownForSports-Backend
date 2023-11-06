package com.example.cs203g1t3.security.Otp;



import com.example.cs203g1t3.exception.InvalidOtpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/otp/")
public class OneTimePasswordController {

    private final OneTimePasswordService oneTimePasswordService;

    @Autowired
    public OneTimePasswordController(OneTimePasswordService oneTimePAsswordService) {
        this.oneTimePasswordService = oneTimePAsswordService;
    }

    @PostMapping("/generateOtp/{userId}")
    private void createOneTimePassword(@PathVariable Long userId) {
        oneTimePasswordService.generateOneTimePassword(userId);
    }

    @PostMapping("/validateOtp")
    private ResponseEntity<?> validateOtp(@RequestBody OneTimePasswordResponse oneTimePasswordResponse){
        Long userId = oneTimePasswordResponse.getUserId();
        int otpInt = oneTimePasswordResponse.getOneTimePasswordCode();
        boolean correctOTP = oneTimePasswordService.checkOneTimePassword(userId,otpInt);
        if (correctOTP) return ResponseEntity.ok().body("OTP is valid, logging in");
        else return ResponseEntity.badRequest().body("OTP is not valid!");

    }
}
