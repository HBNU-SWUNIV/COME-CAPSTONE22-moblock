package com.example.hyperledgervirtualmoneyproject.DTO;

import com.example.hyperledgervirtualmoneyproject.API.UserTradeApi;

import java.util.List;

import lombok.Getter;

@Getter
public class UserTradeHistoryResponseDTO {

    Long totalTradeNumber;
    Long totalPage;

    List<UserTradeResponseDTO> transferResponseList;
}
