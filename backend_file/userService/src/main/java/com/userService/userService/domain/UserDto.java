package com.userService.userService.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {
    private String userEmail;
    private String userName;
    private String password;
    private String role;
    private long phoneNo;
    private String imageName;
}
