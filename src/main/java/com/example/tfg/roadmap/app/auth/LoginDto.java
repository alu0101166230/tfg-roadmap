package com.example.tfg.roadmap.app.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {
    private String username;
    private String email;
    private String password;

}