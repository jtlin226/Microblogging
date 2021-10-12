package com.revature.Micro.controller;

import com.revature.Micro.Entity.MicroUser;
import com.revature.Micro.dto.AuthenticationRequest;
import com.revature.Micro.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;


    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/register")
    public MicroUser createUser(@RequestBody MicroUser microUser){
        log.info("New user being created");
        return userService.saveNewUser(microUser);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticateToken(@RequestBody AuthenticationRequest authReq){
        log.info("User attempting to login.");
        return userService.authenticate(authReq);
    }
}
