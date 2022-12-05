package com.example.hyperledgervirtualmoneyproject.DTO;

import java.util.HashMap;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserGetAssetDTO {
    private String identifier;

    private String owner;

    private HashMap<String, String> coin;
}
