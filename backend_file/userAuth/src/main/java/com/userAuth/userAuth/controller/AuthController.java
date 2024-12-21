package com.userAuth.userAuth.controller;


import com.userAuth.userAuth.exception.UserAlreadyPresentException;
import com.userAuth.userAuth.exception.UserNotFound;
import com.userAuth.userAuth.model.User;
import com.userAuth.userAuth.model.UserDto;
import com.userAuth.userAuth.service.IAuthService;
import com.userAuth.userAuth.service.ITokenGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> loginUser(@RequestBody User user) throws UserNotFound {
        User retrievedUser = authService.login(user);
        System.out.println(retrievedUser);
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
}
