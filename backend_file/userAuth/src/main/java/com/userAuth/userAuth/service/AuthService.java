package com.userAuth.userAuth.service;


import com.userAuth.userAuth.component.Translator;
import com.userAuth.userAuth.config.EcommDTO;
import com.userAuth.userAuth.exception.AuthException;
import com.userAuth.userAuth.exception.ErrorCodes;
import com.userAuth.userAuth.exception.UserAlreadyPresentException;
import com.userAuth.userAuth.exception.UserNotFound;
import com.userAuth.userAuth.model.OtpForgotPass;
import com.userAuth.userAuth.model.User;
import com.userAuth.userAuth.repository.AuthRepo;
import com.userAuth.userAuth.repository.OtpForGotRepo;
import com.userAuth.userAuth.request.CheckForgotPassRequest;
import com.userAuth.userAuth.request.LoginRequest;
import com.userAuth.userAuth.request.PasswordRequest;
import com.userAuth.userAuth.response.CheckOtpPassForgotResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.json.simple.JSONObject;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
public class AuthService implements IAuthService{
    @Autowired
    private AuthRepo authRepo;

    @Autowired
    private OtpForGotRepo otpRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final DirectExchange directExchange;
    private final RabbitTemplate rabbitTemplate;



    @Autowired
    public AuthService(RabbitTemplate rabbitTemplate, DirectExchange directExchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.directExchange = directExchange;
    }

    @Override
    public User addUser(User user) throws UserAlreadyPresentException {
        if(authRepo.findByUserEmail(user.getUserEmail())!=null){
            System.out.println("User Already Present");
            throw new UserAlreadyPresentException();
        }
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        return authRepo.save(user);
    }

    @Override
    public User login(LoginRequest request){

        User existingUser = authRepo.findByUserEmail(request.getEmail());

        // Check if the user exists
        if (existingUser == null) {
            throw new AuthException(ErrorCodes.NOT_FOUND,
                    Translator.toLocale("user.not.found",null));
        }

        // Verify the password
        if (!passwordEncoder.matches(request.getPassword(), existingUser.getPassword())) {
            throw new AuthException(ErrorCodes.CONFLICT,
                    Translator.toLocale("pass.not.matched",null));
        }

        return existingUser;
    }

    @Override
    public User updateUser(String email, User user) throws UserNotFound {
        User userPresent = authRepo.findByUserEmail(email);
        if(authRepo.findByUserEmail(email)!=null){
            User existingUser = authRepo.findById(userPresent.getId()).get();
            // System.out.println(existingUser);
            if(user.getUserName()!=null){
                existingUser.setUserName(user.getUserName());
            }
            if(user.getPhoneNo() != 0L){
                existingUser.setPhoneNo(user.getPhoneNo());
            }
            if(user.getImageName() != null){
                existingUser.setImageName(user.getImageName());
            }
            return authRepo.save(existingUser);
        }
        else {
            throw new UserNotFound();
        }
    }

    @Override
    public void removeUser(String email) throws UserNotFound {
        User user  = authRepo.findById(email).get();
        if(authRepo.findByUserEmail(user.getUserEmail())==null){
         throw new UserNotFound();
       }
        authRepo.delete(user);
        System.out.println("User Removed Successfully..!");
    }


    // Forgot Password
    @RabbitListener(queues = "ForgotPassQueue")
    @Transactional
    @Override
    public boolean emailVerification(String email){
        try{

            Long currentTime = new Date().getTime();

            boolean checkUserExist = authRepo.existsByUserEmailAndActiveTrueAndDeletedFalse(email);

            if (checkUserExist) {
                Optional<OtpForgotPass> otpForgotPassOpt = otpRepo
                        .findByEmailAndActiveAndDeleted(email,true,false);
                int otp = (int) (Math.random() * 9000) + 1000;
                OtpForgotPass otpPass;
                if(otpForgotPassOpt.isPresent()) {
                    otpPass = otpForgotPassOpt.get();
                    otpPass.setOtp(String.valueOf(otp));
                    otpPass.setModifiedAt(currentTime);
                }
                else {
                    otpPass = new OtpForgotPass();
                    otpPass.setOtp(String.valueOf(otp));
                    otpPass.setEmail(email);
                    otpPass.setVerified(false);
                }
                otpRepo.save(otpPass);

                User user = authRepo.findByUserEmail(email);
                otpGenerated(email,String.valueOf(otp),user.getUserName());
                return true;
            }
            else {
                throw new AuthException(ErrorCodes.NOT_FOUND, Translator.toLocale("user.not.found", null));
            }

        }catch (AuthException ex){
            log.error(ExceptionUtils.getStackTrace(ex));
            throw new AuthException(ErrorCodes.INTERNAL_SERVER_ERROR,
                    Translator.toLocale("otp.sent.failed",null));
        }
    }

    private boolean otpGenerated(String email, String otp, String name){

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("to", email);
            jsonObject.put("subject", "Forgot Password OTP");
            jsonObject.put("otp", otp);
            jsonObject.put("name",name);


            EcommDTO ecommDTO = new EcommDTO();
            ecommDTO.setJsonObject(jsonObject);
            rabbitTemplate.convertAndSend(directExchange.getName(), "rabbit-forPass-Key", ecommDTO);
            return true;
        }catch (Exception ex){
            log.error(ExceptionUtils.getStackTrace(ex));
            throw new AuthException(ErrorCodes.INTERNAL_SERVER_ERROR,
                    Translator.toLocale("otp.sent.failed",null));
        }
    }

    @Transactional
    @Override
    public CheckOtpPassForgotResponse forgotPass(CheckForgotPassRequest request){
        try {
            CheckOtpPassForgotResponse response = new CheckOtpPassForgotResponse();

            Long currentTime = new Date().getTime();
            Optional<OtpForgotPass> otpForgotPassOpt = otpRepo
                    .findByEmailAndActiveAndDeleted(request.getEmail(),true, false);

            if(otpForgotPassOpt.isPresent() && StringUtils.isNotBlank(request.getCode())){
                String otpGenerated = otpRepo.findOtpByEmail(request.getEmail());

                if(request.getCode().equals(otpGenerated)){
                    Integer updateValidation = otpRepo.updateForgotPassOTPValidation(request.getEmail(),currentTime);

                    response.setValidated(updateValidation > 0);
                    response.setMessage(
                            updateValidation > 0
                                    ? Translator.toLocale("pass.check.code.success", null)
                                    : Translator.toLocale("pass.check.code.failed", null)
                    );
                }
                else {
                    throw new AuthException(ErrorCodes.CONFLICT,
                            Translator.toLocale("pass.check.code.invalid",null));
                }
            }
            return response;
        }catch (AuthException ex){
            log.error(ExceptionUtils.getStackTrace(ex));
            throw new AuthException(ex.getErrorCode(),ex.getMessage());
        }catch (Exception e){
            log.error(ExceptionUtils.getStackTrace(e));
            throw new AuthException(ErrorCodes.NOT_FOUND,
                    Translator.toLocale("pass.check.code.failed",null));
        }
    }

    @Transactional
    @Override
    public boolean changePassword(PasswordRequest request) {
        if (StringUtils.isBlank(request.getEmail()) || StringUtils.isBlank(request.getNewPassword())) {
            throw new AuthException(ErrorCodes.NO_CONTENT,
                    Translator.toLocale("must.not.be.blank",null));
        }

        // Hash the new password before saving it to the database
        String hashedPassword = passwordEncoder.encode(request.getNewPassword());

        // Update the password
        int updatePass = authRepo.updatePassword(request.getEmail(), hashedPassword);

        return updatePass > 0;
    }
}
