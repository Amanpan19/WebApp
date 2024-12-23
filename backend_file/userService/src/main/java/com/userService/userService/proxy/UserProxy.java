package com.userService.userService.proxy;


import com.userService.userService.domain.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "authService",url = "http://localhost:8081")
public interface UserProxy {
    @PostMapping("api/v1/authService/addUser")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto);

    @PutMapping("api/v1/authService/user/Update/{email}")
    public ResponseEntity<?> updateUser(@RequestBody UserDto userDto,@PathVariable String email);
}
