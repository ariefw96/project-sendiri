package com.sendiri.microservices.demoproject.service;

import com.sendiri.microservices.demoproject.model.AuthModel;
import com.sendiri.microservices.demoproject.repository.AuthRepository;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class AuthService {

    @Autowired
    private Environment env;

    @Autowired
    AuthRepository authRepository;

    public boolean checkJwt(String token) {
        String secretKey = Base64.getEncoder().encodeToString(env.getProperty("secret.key").getBytes());
        try {
            Jwts.parser().setSigningKey(secretKey).parse(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public AuthModel findUser(String username, String password){
        return authRepository.findUser(username, password);
    }

    public void register(AuthModel authModel){
        authRepository.save(authModel);
    }

    public void updateJwt(AuthModel authModel){
        authRepository.save(authModel);
    }
}
