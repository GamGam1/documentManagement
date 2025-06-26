package com.gamyA.documentManagement.service;

import com.gamyA.documentManagement.DTOs.UserLogIn;
import com.gamyA.documentManagement.DTOs.UserRegister;
import com.gamyA.documentManagement.entity.UserInfo;
import com.gamyA.documentManagement.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepo userRepo;

    private PasswordEncoder passwordEncoder;

    private AuthenticationManager authenticationManager;

    private  JWTService jwtService;

    @Autowired
    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JWTService jwtService) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public void registerUser(UserRegister userRegister){
        userRepo.save(
                new UserInfo(
                        passwordEncoder.encode(userRegister.getPassword()),
                        userRegister.getUsername()
                )
        );
    }

    public String AuthAndGetToken(UserLogIn userLogIn){
        try{
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLogIn.getUsername(), userLogIn.getPassword())
            );
            UserInfo userInfo = userRepo.findByUsername(userLogIn.getUsername()).orElseThrow();
            return jwtService.generateToken(userInfo);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
