package com.userAuth.userAuth.repository;


import com.userAuth.userAuth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepo extends JpaRepository<User,String> {
    User findByUserEmailAndPassword(String userEmail, String password);
    User findByUserEmail(String userEmail);
}
