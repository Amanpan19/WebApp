package com.userAuth.userAuth.service;


import com.userAuth.userAuth.exception.UserAlreadyPresentException;
import com.userAuth.userAuth.exception.UserNotFound;
import com.userAuth.userAuth.model.User;
import com.userAuth.userAuth.repository.AuthRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements IAuthService{
    @Autowired
    private AuthRepo authRepo;

    @Override
    public User addUser(User user) throws UserAlreadyPresentException {
        if(authRepo.findByUserEmail(user.getUserEmail())!=null){
            System.out.println("User Already Present");
            throw new UserAlreadyPresentException();
        }
            return authRepo.save(user);
    }

    @Override
    public User login(User user) throws UserNotFound {
        if(authRepo.findByUserEmail(user.getUserEmail())==null){
            System.out.println("User Not present");
            throw new UserNotFound();
        }
        else
           return authRepo.findByUserEmailAndPassword(user.getUserEmail(), user.getPassword());
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
}
