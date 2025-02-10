package com.userAuth.userAuth.repository;


import com.userAuth.userAuth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepo extends JpaRepository<User,String> {

    User findByUserEmailAndPassword(String userEmail, String password);

    User findByUserEmail(String userEmail);

    boolean existsByUserEmailAndActiveTrueAndDeletedFalse(String email);

    @Modifying
    @Query(value = "Update user Set password = :password " +
            "where user_email = :email And active = true And deleted = false",nativeQuery = true)
    Integer updatePassword(String email, String password);

}
