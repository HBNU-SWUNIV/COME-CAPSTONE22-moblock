package com.example.hyperledgervirtualmoneyproject.DTO;

import java.util.List;

import lombok.Getter;

@Getter
public class UserShopListResponseDTO {

    List<UserShopListDTO> storeResponseList;
    Long totalPage;
    Long totalUserNumber;
}
