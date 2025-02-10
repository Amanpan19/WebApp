package com.userAuth.userAuth.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class User extends BaseEntity{

    private String userEmail;
    private String userName;
    private String password;
    private String role;
    private long phoneNo;
    private String imageName;
    private String gender;

}
