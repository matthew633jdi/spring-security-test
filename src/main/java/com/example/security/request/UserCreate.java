package com.example.security.request;

import lombok.Data;

@Data
public class UserCreate {
    private String username;
    private String password;
    private String email;
}
