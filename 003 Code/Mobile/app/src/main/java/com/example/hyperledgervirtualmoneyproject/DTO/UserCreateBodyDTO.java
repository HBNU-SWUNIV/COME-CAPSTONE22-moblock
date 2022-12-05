package com.example.hyperledgervirtualmoneyproject.DTO;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserCreateBodyDTO {
    private String identifier;
    private String password;
    private String name;
    private String userRole;
}
