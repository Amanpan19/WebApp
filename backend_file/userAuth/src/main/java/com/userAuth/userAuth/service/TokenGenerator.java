package com.userAuth.userAuth.service;


import com.userAuth.userAuth.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class TokenGenerator implements ITokenGenerator {

    @Value("${jwt.main.key}")
    private String secretKey;

    @Override
    public Map<String, String> storeToken(User user) {
        Map<String,Object> userData = new HashMap<>();
        userData.put("email", user.getUserEmail());
        userData.put("role", user.getRole());

        String token = Jwts.builder()
                .setClaims(userData)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(SignatureAlgorithm.HS512,secretKey)
                .compact();

        Map<String,String> genToken = new HashMap<>();

        genToken.put("Token",token);
        genToken.put("email",user.getUserEmail());
        genToken.put("name",user.getUserName());
        genToken.put("role", user.getRole());

        return genToken;
    }
}
