package com.example.hyperledgervirtualmoneyproject.DTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtToken {
    private static String jwt;
    private static String Id;

    public static String getJwt() {
        return jwt;
    }

    public static void setToken(String jwt){
        JwtToken.jwt = jwt;
    }

    public static String getId() {
        return Id;
    }

    public static void setId(String id) {
        Id = id;
    }
}
