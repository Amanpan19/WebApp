package com.userAuth.userAuth.controller;


import com.userAuth.userAuth.component.Translator;
import com.userAuth.userAuth.cover.AuthResponse;
import com.userAuth.userAuth.cover.ResponseHelper;
import com.userAuth.userAuth.exception.UserAlreadyPresentException;
import com.userAuth.userAuth.exception.UserNotFound;
import com.userAuth.userAuth.model.User;
import com.userAuth.userAuth.model.UserDto;
import com.userAuth.userAuth.request.CheckForgotPassRequest;
import com.userAuth.userAuth.request.LoginRequest;
import com.userAuth.userAuth.request.PasswordRequest;
import com.userAuth.userAuth.response.CheckOtpPassForgotResponse;
import com.userAuth.userAuth.service.IAuthService;
import com.userAuth.userAuth.service.ITokenGenerator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/authService")
public class AuthController {

    private IAuthService authService;
    private ITokenGenerator tokenGenerator;
    private ModelMapper modelMapper;

    @Autowired
    public AuthController(IAuthService authService, ITokenGenerator tokenGenerator, ModelMapper modelMapper) {
        this.authService = authService;
        this.tokenGenerator = tokenGenerator;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/addUser")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) throws UserAlreadyPresentException {
        User user = modelMapper.map(userDto, User.class);
        return new ResponseEntity<>(authService.addUser(user), HttpStatus.OK);
    }
    @PostMapping("/user/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest request) throws UserNotFound {
        User retrievedUser = authService.login(request);

        log.info("Retrieved User : {}",retrievedUser);

        if(retrievedUser!=null){
            return new ResponseEntity<>(tokenGenerator.storeToken(retrievedUser),HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Authorization Failed",HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/user/Update/{email}")
    public ResponseEntity<?> updateUser(@RequestBody UserDto userDto,@PathVariable String email) throws UserNotFound {
        System.out.println("User DTO : "+userDto);
        User user = modelMapper.map(userDto, User.class);
        return new ResponseEntity<>(authService.updateUser(email,user),HttpStatus.OK);
    }

    @DeleteMapping("/user/deleteUser/{email}")
    public ResponseEntity<?> removeUser(@PathVariable String email) throws UserNotFound {
        authService.removeUser(email);
        return new ResponseEntity<>("User Removed..!",HttpStatus.OK);
    }

    @GetMapping("/user/email/verify")
    @SuppressWarnings("unchecked")
    public AuthResponse<Boolean> emailVerification(@RequestParam String email) throws UserNotFound {

        boolean response = authService.emailVerification(email);
        return ResponseHelper.createResponseForFlags(new AuthResponse<Boolean>(), response,
                Translator.toLocale("otp.sent.success", null),
                Translator.toLocale("email.verified.failed", null));
    }

    @PutMapping("/forgot/pw/otp-verification")
    @SuppressWarnings("unchecked")
    public AuthResponse<CheckOtpPassForgotResponse> otpVerification(@RequestBody CheckForgotPassRequest request){

        CheckOtpPassForgotResponse response = authService.forgotPass(request);
        return ResponseHelper.createResponse(new AuthResponse<CheckOtpPassForgotResponse>(), response,
                Translator.toLocale("pass.check.code.success", null),
                Translator.toLocale("pass.check.code.failed", null));
    }

    @PutMapping("/change/password")
    @SuppressWarnings("unchecked")
    public AuthResponse<Boolean> changePassword(@RequestBody PasswordRequest request){
        boolean response = authService.changePassword(request);
        return ResponseHelper.createResponse(new AuthResponse<Boolean>(), response,
                Translator.toLocale("password.changed.success",null),
                Translator.toLocale("password.changed.failed",null));
    }
}
