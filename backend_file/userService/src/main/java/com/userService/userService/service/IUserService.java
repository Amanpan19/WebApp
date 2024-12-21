package com.userService.userService.service;


import com.userService.userService.domain.FavItems;
import com.userService.userService.domain.User;
import com.userService.userService.exception.UserAlreadyExist;
import com.userService.userService.exception.UserNotFoundException;

public interface IUserService {
  User addUser(User user) throws UserAlreadyExist;
  User updateUser(String email,User user) throws UserNotFoundException;
  User addItemInList(String email, FavItems favItems);
  boolean itemExist(String email,int itemId);
  void removeItemFromFav(String email,int itemId);
  byte[] getUserImage(String email)throws UserNotFoundException;
  String getUserName(String email)throws UserNotFoundException;
  User getUserData(String email) throws UserNotFoundException;
}
