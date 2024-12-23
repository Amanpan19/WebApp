package com.userService.userService.service;


import com.userService.userService.config.EcommDTO;
import com.userService.userService.domain.FavItems;
import com.userService.userService.domain.User;
import com.userService.userService.domain.UserDto;
import com.userService.userService.exception.UserAlreadyExist;
import com.userService.userService.exception.UserNotFoundException;
import com.userService.userService.proxy.UserProxy;
import com.userService.userService.repository.UserRepo;
import org.json.simple.JSONObject;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserService implements IUserService{

    private final UserRepo userRepo;
    private final UserProxy userProxy;
    private final RabbitTemplate rabbitTemplate;
    private final DirectExchange directExchange;


    @Autowired
    public UserService(UserRepo userRepo, UserProxy userProxy, RabbitTemplate rabbitTemplate, DirectExchange directExchange) {
        this.userRepo = userRepo;
        this.userProxy = userProxy;
        this.rabbitTemplate = rabbitTemplate;
        this.directExchange = directExchange;
    }

//    @RabbitListener(queues = "EmailQueue")
    @Override
    public User addUser(User user) throws UserAlreadyExist {
        System.out.println("add user in service");
        if(userRepo.existsById(user.getUserEmail())){
            System.out.println("User already exist!!"); // replaced by exception
            throw new UserAlreadyExist();
        }
        UserDto userDto = new UserDto();
        userDto.setUserEmail(user.getUserEmail());
        userDto.setPassword(user.getPassword());
        userDto.setUserName(user.getUserName());
        userDto.setPhoneNo(user.getPhoneNo());
        userDto.setImageName(user.getImageName());
        userDto.setRole(user.getRole());

        System.out.println(userDto);

        userProxy.registerUser(userDto);
        User savedUser = userRepo.save(user);

        String message = "Dear " + user.getUserName() + ",\n\n" +
                "Thank you for joining CloudCart! We are delighted to have you.\n\n" +
                user.getUserName()+ ", If you have any questions or need assistance, feel free to reach out to our support team.\n\n" +
                "Once again, welcome to CloudCart!\n\n" +
                "Best regards,\n" +
                "Team CloudCart\n";

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("to",user.getUserEmail());
        jsonObject.put("subject","Welcome to CloudCart!");
        jsonObject.put("message",message);

        EcommDTO ecommDTO = new EcommDTO();
        ecommDTO.setJsonObject(jsonObject);
        rabbitTemplate.convertAndSend(directExchange.getName(),"Cloud-Key",ecommDTO);

        System.out.println("success :" + directExchange.getName());

        return savedUser;
    }

    @Override
    public User updateUser(String email, User user) throws UserNotFoundException {
        if(userRepo.findById(email).isEmpty()){
            System.out.println("user not found");
            throw new UserNotFoundException();
        }
        User existingUser = userRepo.findById(email).get();
        UserDto userDto = new UserDto();

        if(user.getUserImage() != null){
            existingUser.setUserImage(user.getUserImage());
            existingUser.setImageName(user.getImageName());
            userDto.setImageName(user.getImageName());
        }
        if(user.getUserName() != null){
            existingUser.setUserName(user.getUserName());
            userDto.setUserName(user.getUserName());
        }
        if(user.getPhoneNo() != 0){
            existingUser.setPhoneNo(user.getPhoneNo());
            userDto.setPhoneNo(user.getPhoneNo());
        }
        userProxy.updateUser(userDto,email);
        return userRepo.save(existingUser);
    }

    @Override
    public User addItemInList(String email, FavItems favItems) {
        User existingUser = userRepo.findById(email).get();
        boolean userExist = false;
        if(existingUser.getFavItems() != null){
            for(FavItems items : existingUser.getFavItems()){
                if(items.getItemId()==favItems.getItemId()){
                    userExist = true;
                    break;
                }
            }
        }
        if(!userExist){
            if (existingUser.getFavItems()!=null){
                existingUser.setFavItems(new ArrayList<>());
            }
            existingUser.getFavItems().add(favItems);
        }
        userRepo.save(existingUser);
        return existingUser;
    }

    @Override
    public boolean itemExist(String email, int itemId) {
        if(userRepo.findById(email).isPresent()){
            List<FavItems> favList = userRepo.findById(email).get().getFavItems();
            for(FavItems items : favList){
                if(items.getItemId()==itemId){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void removeItemFromFav(String email, int itemId) {
      User user = userRepo.findById(email).get();
      List<FavItems> itemList = userRepo.findById(email).get().getFavItems();
      for(FavItems favItems : itemList){
          if(favItems.getItemId()==itemId){
              itemList.remove(favItems);
              System.out.println("Item Removed Successfully..!");
              userRepo.save(user);
              break;
          }
      }
    }
    @Override
    public byte[] getUserImage(String email)throws UserNotFoundException {
        if(userRepo.findById(email).isEmpty()){
            System.out.println("User not exist"); // user defined exception
            throw new UserNotFoundException();
        }
        User user = userRepo.findById(email).get();
        return user.getUserImage();
    }

    @Override
    public String getUserName(String email)throws UserNotFoundException {
        if(userRepo.findById(email).isEmpty()){
            System.out.println("User not exist"); // user defined exception
            throw new UserNotFoundException();
        }
        User user = userRepo.findById(email).get();
        return user.getUserName();
    }

    @Override
    public User getUserData(String email)throws UserNotFoundException {
        if(userRepo.findById(email).isEmpty()){
            System.out.println("User not exist"); // user defined exception
            throw new UserNotFoundException();
        }
        User user = userRepo.findById(email).get();
        return user;
    }
}
