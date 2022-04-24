package com.sendiri.microservices.demoproject.controller;


import com.sendiri.microservices.demoproject.json.request.AuthRequest;
import com.sendiri.microservices.demoproject.model.AuthModel;
import com.sendiri.microservices.demoproject.service.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.Date;

@RestController
@RequestMapping("api/v1/auth")
public class authController {

    private static final Logger log = LoggerFactory.getLogger(authController.class);

    @Autowired
    AuthService authService;

    @Autowired
    Environment env;

    @PostMapping("login")
    public ResponseEntity<Object> loginUser(@RequestBody  AuthRequest authRequest){
        log.info("Login request {} ", authRequest);
        String secretKey = Base64.getEncoder().encodeToString(env.getProperty("secret.key").getBytes());
        try{
            AuthModel authModel = authService.findUser(authRequest.getUsername(), authRequest.getPassword());
            if(authModel == null){
                return ResponseEntity.status(401).body("Username / password salah");
            }
            Date now = new Date();
            Date valid = new Date(now.getTime() + 36000000); //valid for 10 hour
            String Jwtdata = Jwts.builder().setHeaderParam("typ","JWT")
                    .claim("id",authModel.getId()).claim("username",authModel.getUsername()).claim("isLogin", true).setIssuedAt(now).setExpiration(valid).signWith(SignatureAlgorithm.HS256, secretKey).compact();
            authModel.setToken(Jwtdata);
            authService.updateJwt(authModel);
            return ResponseEntity.ok().body(Jwtdata);
        }catch (Exception x){
           log.error(x.getLocalizedMessage());
            return null;
        }
    }

    @PostMapping("register")
    public ResponseEntity<Object> registerUser(@RequestBody AuthRequest authRequest){
        log.info("Register request {}",authRequest);
        try{
            AuthModel authModel = new AuthModel();
            authModel.setUsername(authRequest.getUsername());
            authModel.setPassword(authRequest.getPassword());
            authModel.setToken("");
            authService.register(authModel);
            return ResponseEntity.ok().body("sukses register dengan username "+authRequest.getUsername());
        }catch (Exception x){
            log.error(x.getLocalizedMessage());
            return null;
        }
    }

    @GetMapping("verify")
    public Boolean verifyJwt(@RequestParam("jwt") String jwt){
        return authService.checkJwt(jwt);
    }

}
