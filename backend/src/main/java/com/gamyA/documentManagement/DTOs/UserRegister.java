package com.gamyA.documentManagement.DTOs;

public class UserRegister {

    private String username;

    private String password;

    public UserRegister() {
    }

    public UserRegister(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
