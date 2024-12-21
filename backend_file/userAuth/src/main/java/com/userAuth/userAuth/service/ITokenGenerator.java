package com.userAuth.userAuth.service;



import com.userAuth.userAuth.model.User;

import java.util.Map;

public interface ITokenGenerator {
    Map<String,String>storeToken(User user);
}
