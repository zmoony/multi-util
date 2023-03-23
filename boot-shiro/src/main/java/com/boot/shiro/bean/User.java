package com.boot.shiro.bean;

import lombok.Data;

@Data
public class User {
    public Integer id;
    public String username;
    public String password;
    public String role;
    public String state;
}