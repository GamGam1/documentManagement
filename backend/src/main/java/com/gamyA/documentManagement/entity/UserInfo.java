package com.gamyA.documentManagement.entity;

import jakarta.persistence.*;

@Entity
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long UserId;
    private String password;
    @Column(unique = true)
    private String username;
    private String role = "ROLE_USER";

    public UserInfo() {
    }

    public UserInfo(String password, String username) {
        this.password = password;
        this.username = username;
    }

    public Long getUserId() {
        return UserId;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
