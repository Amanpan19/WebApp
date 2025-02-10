package com.userAuth.userAuth.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CheckOtpPassForgotResponse {
    private String message;
    private boolean validated;
}
