package com.example.hyperledgervirtualmoneyproject.API;


import com.example.hyperledgervirtualmoneyproject.DTO.UserTradeHistoryResponseDTO;
import com.example.hyperledgervirtualmoneyproject.DTO.UserTradeResponseDTO;
import com.example.hyperledgervirtualmoneyproject.DTO.UserTransferDTO;
import com.example.hyperledgervirtualmoneyproject.DTO.UserTransferRequestDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserTradeApi {

    @POST("/user/trade")
    Call<UserTransferDTO> transfer(
            @Header("Authorization") String Authorization,
            @Body UserTransferRequestDTO userTransferRequestDTO
    );

    @GET("/user/trade")
    Call<UserTradeHistoryResponseDTO>trade(
            @Header("Authorization") String Authorization,
            @Query("page") int page
    );
}
