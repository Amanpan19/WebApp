package com.userAuth.userAuth.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "otp_forgot_pass")
public class OtpForgotPass extends BaseEntity{
    private String otp;
    private String email;
    private boolean verified;
}
