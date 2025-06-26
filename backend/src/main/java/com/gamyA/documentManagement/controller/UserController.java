package com.gamyA.documentManagement.controller;

import com.gamyA.documentManagement.DTOs.UserLogIn;
import com.gamyA.documentManagement.DTOs.UserRegister;
import com.gamyA.documentManagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/auth")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/register")
    public String registerUser(@RequestBody UserRegister userRegister){
        userService.registerUser(userRegister);
        return "ok";
    }

    @PostMapping(value = "/login")
    public String userLogIn(@RequestBody UserLogIn userLogIn){
        return userService.AuthAndGetToken(userLogIn);
    }
}
