package com.Cart_Service.cartService.Repository;


import com.Cart_Service.cartService.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepo extends JpaRepository<User, String> {
    @Query(value="Select * from user where user_email=:user_email",nativeQuery = true)
    User getUserByUserEmail(@Param("user_email") String userEmail);

    User findByIdAndUserEmail(String id,String userEmail);

}
