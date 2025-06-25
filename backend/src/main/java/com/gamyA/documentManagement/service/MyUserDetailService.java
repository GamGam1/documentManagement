package com.gamyA.documentManagement.service;

import com.gamyA.documentManagement.entity.UserInfo;
import com.gamyA.documentManagement.entity.UserPrincipal;
import com.gamyA.documentManagement.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailService implements UserDetailsService {

    private UserRepo userRepo;

    @Autowired
    public MyUserDetailService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo userInfo =userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        return new UserPrincipal(userInfo);
    }
}
