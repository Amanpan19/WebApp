package com.userAuth.userAuth.service;


import com.userAuth.userAuth.exception.UserAlreadyPresentException;
import com.userAuth.userAuth.exception.UserNotFound;
import com.userAuth.userAuth.model.User;

public interface IAuthService {
    User addUser(User user) throws UserAlreadyPresentException;
    User login(User user) throws UserNotFound;
    User updateUser(String email,User user) throws UserNotFound;
    void removeUser(String email) throws UserNotFound;
}
