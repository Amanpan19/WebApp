package com.userAuth.userAuth.repository;

import com.userAuth.userAuth.model.OtpForgotPass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OtpForGotRepo extends JpaRepository<OtpForgotPass,String> {

    Optional<OtpForgotPass> findByEmailAndActiveAndDeleted(String email, boolean active, boolean deleted);

    @Query(value ="Select otp from otp_forgot_pass where email = :email And active = true And deleted = false And verified=false" ,nativeQuery = true)
    String findOtpByEmail(String email);

    @Modifying
    @Query(value ="Update otp_forgot_pass SET active=false, " +
            "deleted=true, verified=true, modified_at=:currentTime " +
            "Where email=:email And active=true And deleted=false" ,nativeQuery = true)
    Integer updateForgotPassOTPValidation(String email, Long currentTime);
}

