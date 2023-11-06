package com.example.cs203g1t3.security.Otp;

import com.example.cs203g1t3.exception.NoSuchUserFoundException;
import com.example.cs203g1t3.models.User;
import com.example.cs203g1t3.repository.UserRepository;
import com.example.cs203g1t3.security.Email.EmailDetails;
import com.example.cs203g1t3.security.Email.EmailServiceImpl;
import com.example.cs203g1t3.security.Otp.OneTimePassword;
import com.example.cs203g1t3.security.Otp.OneTimePasswordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Email;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;

@Service
public class OneTimePasswordService {

    private final Long expiryInterval = 1L * 60 * 1000;

    private final OneTimePasswordRepository oneTimePasswordRepository;

    private final UserRepository userRepository;

    private final static Integer LENGTH = 6;

    private final EmailServiceImpl emailService;


    @Autowired
    public OneTimePasswordService(OneTimePasswordRepository oneTimePasswordRepository, UserRepository userRepository,EmailServiceImpl emailService) {
        this.oneTimePasswordRepository = oneTimePasswordRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    //Clear any existing one time passwords
    public void clearOneTimePassword(Long userId){
        try{
            User user = userRepository.findById(userId).get();
            user.setOneTimePassword(null);
        } catch (NoSuchElementException e){
            throw new NoSuchUserFoundException("No Such User Found!");
        }
    }

    //Creates and set one time password within user object while returning the one time password
    public void generateOneTimePassword(Long userId) {
        OneTimePassword oneTimePassword = new OneTimePassword();

        oneTimePassword.setOneTimePasswordCode(createRandomOneTimePassword().get());
        oneTimePassword.setExpires(new Date(System.currentTimeMillis() + expiryInterval));
        User user;
        try{
            user = userRepository.findById(userId).get();
            oneTimePassword.setUser(user);
            user.setOneTimePassword(oneTimePassword);
            oneTimePasswordRepository.save(oneTimePassword);
        } catch (NoSuchElementException e){
            throw new NoSuchUserFoundException("No Such User Found!");
        }
        String emailMessage = "Hello this is your One Time Password:\n\n" + oneTimePassword.getOneTimePasswordCode()
                + "\n\n Thank you!\nDownForSports";
        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setRecipient(user.getEmail());
        emailDetails.setMsgBody(emailMessage);
        emailService.sendSimpleMail(emailDetails);
    }
    //Supply generateOneTimePassword with LENGTH = 6 code
    public static Supplier<Integer> createRandomOneTimePassword() {
        return () -> {
            Random random = new Random();
            StringBuilder oneTimePassword = new StringBuilder();
            for (int i = 0; i < LENGTH; i++) {
                int randomNumber = random.nextInt(10);
                oneTimePassword.append(randomNumber);
            }
            return Integer.parseInt(oneTimePassword.toString().trim());
        };
    }

    //Checks the one time password code for validity and check for expiry
    public Boolean checkOneTimePassword(Long userId,int oneTimePasswordCode){
        User user = userRepository.findById(userId).get();
        OneTimePassword oneTimePassword = user.getOneTimePassword();
        Date now = new Date(System.currentTimeMillis());
        //Validate the code and checks for expiry
        if(oneTimePasswordCode == oneTimePassword.getOneTimePasswordCode()){
            if(oneTimePassword.getExpires().compareTo(now) > 0){
                oneTimePasswordRepository.deleteById(oneTimePassword.getId());
                return true;
            }
        }
        return false;
    }
}
