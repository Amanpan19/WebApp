package com.userService.userService.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.userService.userService.domain.FavItems;
import com.userService.userService.domain.User;
import com.userService.userService.exception.UserAlreadyExist;
import com.userService.userService.exception.UserNotFoundException;
import com.userService.userService.service.ISupplierService;
import com.userService.userService.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/userService")
public class UserController {

    private final UserService userService;
    private final ISupplierService iSupplierService;

    @Autowired
    public UserController(UserService userService, ISupplierService iSupplierService) {
        this.userService = userService;
        this.iSupplierService = iSupplierService;
    }

    @PostMapping("/register/user")
    public ResponseEntity<?> addUser(@RequestParam("file") MultipartFile file,@RequestParam("userData")String user) throws IOException, UserAlreadyExist {
        System.out.println("request reached to register");
        User user1 = new ObjectMapper().readValue(user, User.class);
        user1.setUserImage(file.getBytes());
        System.out.println(user);
        String fileName = file.getOriginalFilename();
        String newFileName = FilenameUtils.getBaseName(fileName)+"_"+System.currentTimeMillis()+"."+FilenameUtils.getExtension(fileName);
        user1.setImageName(newFileName);
        System.out.println(newFileName);
           return new ResponseEntity<>(userService.addUser(user1), HttpStatus.CREATED);
    }


    @PutMapping("/update/user")
    public ResponseEntity<?> updateUser(HttpServletRequest request,@RequestParam(value = "file",required = false) MultipartFile file,@RequestParam("userData")String user) throws IOException, UserNotFoundException {
        String email = (String)request.getAttribute("attr1"); //attr1=email
        System.out.println("user details: "+user);
        User user1 = new ObjectMapper().readValue(user,User.class);
        System.out.println(email);
        if(file!=null && !file.isEmpty()){
            user1.setUserImage(file.getBytes());
            String fileName = file.getOriginalFilename();
            String newFileName = FilenameUtils.getBaseName(fileName)+"_"+System.currentTimeMillis()+"."+FilenameUtils.getExtension(fileName);
            user1.setImageName(newFileName);
        }
        System.out.println("User Email : "+user1.getUserEmail()+", user image_Name : "+user1.getImageName()+", phone-No : "+user1.getPhoneNo()+
        ", ");
        return new ResponseEntity<>(userService.updateUser(email,user1),HttpStatus.OK);
    }

    @PostMapping("/addFav/Item")
    public ResponseEntity<?>addFavItem(HttpServletRequest request, FavItems favItems){
        String email = (String)request.getAttribute("attr1");
        if(email.isEmpty()){
            System.out.println("Email is empty");
            return new ResponseEntity<>("No Value we are getting",HttpStatus.BAD_REQUEST);
        }
        else {
            return new ResponseEntity<>(userService.addItemInList(email,favItems),HttpStatus.OK);
        }
    }


    @GetMapping("/item/check")
    public ResponseEntity<?>checkItemExist(HttpServletRequest servletRequest,@RequestParam int itemId){
        String email = (String)servletRequest.getAttribute("attr1");
        if(email.isEmpty()){
            return new ResponseEntity<>("Email is empty, check it again..!",HttpStatus.BAD_REQUEST);
        }
        else {
            return new ResponseEntity<>(userService.itemExist(email,itemId),HttpStatus.OK);
        }
    }

    @DeleteMapping("/remove/Item")
    public ResponseEntity<?> removeItem(HttpServletRequest request, @RequestParam int itemId){
        String email = (String)request.getAttribute("attr1");
        if(email.isEmpty()){
            return new ResponseEntity<>("check your email again..!",HttpStatus.BAD_REQUEST);
        }
        else {
            userService.removeItemFromFav(email,itemId);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @GetMapping("/get/userImage")
    public ResponseEntity<?> getUserImage(HttpServletRequest request) throws UserNotFoundException {
        String email = (String)request.getAttribute("attr1");
        byte[] imageData = userService.getUserImage(email);
        if (imageData==null || email==null || email.isEmpty()){
            return new ResponseEntity<>("check your email again..!",HttpStatus.NOT_FOUND);
        }
        String base64Image = Base64.getEncoder().encodeToString(imageData);
        Map<String,Object> response = new HashMap<>();
        response.put("imageData",base64Image);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/get/userName")
    public ResponseEntity<?> getUserName(HttpServletRequest request) throws UserNotFoundException {
        String email = (String)request.getAttribute("attr1");
        if(email.isEmpty()){
            return new ResponseEntity<>("check your email again..!",HttpStatus.BAD_REQUEST);
        }
        else {
            return new ResponseEntity<>(userService.getUserName(email),HttpStatus.OK);
        }
    }

    @GetMapping("/get/userData")
    public ResponseEntity<?> getUserData(HttpServletRequest request) throws UserNotFoundException {
        String email = (String)request.getAttribute("attr1");
        if(email.isEmpty()){
            return new ResponseEntity<>("check your email again..!",HttpStatus.BAD_REQUEST);
        }
        else {
            return new ResponseEntity<>(userService.getUserData(email),HttpStatus.OK);
        }
    }

    // http://localhost:9000/api/v1/userService/add/supplier/{email}
    @GetMapping("/add/supplier/{email}")
    public ResponseEntity<?> addSupplier(@PathVariable String email) throws UserNotFoundException {
        System.out.println(email);
        User user  = userService.getUserData(email);
        if(user==null){
            throw new UserNotFoundException();
        }
        else {
            return new ResponseEntity<>(iSupplierService.addSupplier(user),HttpStatus.OK);
        }
    }

    //  http://localhost:9000/api/v1/userService/getSupplier/waiting
    @GetMapping("/getSupplier/waiting")
    public ResponseEntity<?> waitingForApproval(HttpServletRequest httpServletRequest){
        String email = (String)httpServletRequest.getAttribute("attr1");
        if(email.isEmpty()){
            return new ResponseEntity<>("User Not Found",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(iSupplierService.getSuppliersWaiting(),HttpStatus.OK);
    }

    // http://localhost:9000/api/v1/userService/getSupplier/image/{email}
    @GetMapping("/getSupplier/image/{email}")
    public ResponseEntity<?> waitingUserImg(@PathVariable String email) throws UserNotFoundException {
        byte[] imageData = iSupplierService.getUserImage(email);
        if(imageData==null || email==null || email.isEmpty()){
            return new ResponseEntity<>("User Not Found",HttpStatus.NOT_FOUND);
        }
        String base64Image = Base64.getEncoder().encodeToString(imageData);
        Map<String,Object> response = new HashMap<>();
        response.put("imageData",base64Image);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    // http://localhost:9000/api/v1/userService/approveOrDeny/{email}/{supType}
    @GetMapping("/approveOrDeny")
    public ResponseEntity<?>approveOrDenyReq(HttpServletRequest httpServletRequest,@RequestParam String email, @RequestParam boolean supType){
        String adminRole = (String)httpServletRequest.getAttribute("attr2");
        System.out.println("Role ===> "+adminRole);
        if(adminRole.isEmpty()){
            System.out.println("You are Not Authorized");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        else {
            iSupplierService.approveOrNot(email,supType);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
    // http://localhost:9000/api/v1/userService/getUserRole
    @GetMapping("/getUserRole")
    public ResponseEntity<?>getSupplierRole(HttpServletRequest httpServletRequest)throws UserNotFoundException{
        String email = (String)httpServletRequest.getAttribute("attr1");
        return new ResponseEntity<>(iSupplierService.supplierRole(email),HttpStatus.OK);
    }
}
