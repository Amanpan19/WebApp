package com.userAuth.userAuth.service;


import com.userAuth.userAuth.exception.UserAlreadyPresentException;
import com.userAuth.userAuth.exception.UserNotFound;
import com.userAuth.userAuth.model.User;
import com.userAuth.userAuth.request.CheckForgotPassRequest;
import com.userAuth.userAuth.request.LoginRequest;
import com.userAuth.userAuth.request.PasswordRequest;
import com.userAuth.userAuth.response.CheckOtpPassForgotResponse;

public interface IAuthService {
    User addUser(User user) throws UserAlreadyPresentException;

    /**
     * This method is used for login
     *
     * @param request
     * @return
     * */
    User login(LoginRequest request);
    User updateUser(String email,User user) throws UserNotFound;
    void removeUser(String email) throws UserNotFound;


    /**
     * This method is going to check whether this user exist in db or not.
     * If exist then OTP send.
     *
     * @param email
     * @return
     * */
    boolean emailVerification(String email);
    /**
     * This method will allow the user to move to next step to change the password.
     *
     * @param request
     * @return CheckOtpPassForgotResponse
     * */
    CheckOtpPassForgotResponse forgotPass(CheckForgotPassRequest request);

    /**
     * This method is going the update the previous password with the new password.
     *
     * @param request
     * @return
     * */
    boolean changePassword(PasswordRequest request);
}
