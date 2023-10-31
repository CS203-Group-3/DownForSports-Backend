package com.example.cs203g1t3.security.Otp;



import com.example.cs203g1t3.exception.InvalidOtpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/otp")
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
    private Boolean validateOtp(@RequestBody OneTimePasswordResponse oneTimePasswordResponse){
        Long userId = oneTimePasswordResponse.getUserId();
        int otpInt = oneTimePasswordResponse.getOneTimePasswordCode();
            System.out.println(otpInt);
        try{
            return oneTimePasswordService.checkOneTimePassword(userId,otpInt);
        } catch (RuntimeException e){
            throw new InvalidOtpException("OTP is not valid!");
        }
    }
}
