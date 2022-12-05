package com.example.hyperledgervirtualmoneyproject.API;


import com.example.hyperledgervirtualmoneyproject.DTO.UserCreateBodyDTO;
import com.example.hyperledgervirtualmoneyproject.DTO.UserGetAssetDTO;
import com.example.hyperledgervirtualmoneyproject.DTO.UserLoginDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface UserApi {

    @GET("/user/user")
    Call<UserGetAssetDTO> getAsset(@Header("Authorization") String Authorization);

    @POST("/user/user")
    Call<UserLoginDTO> join(@Body UserCreateBodyDTO userCreateBodyDTO);

    @GET("/user/login")
    Call<UserLoginDTO> login(
            @Query("identifier") String identifier,
            @Query("password") String password
    );

    @PATCH("/user/user")
    Call<Void> changePassword(
            @Header("Authorization") String authorization,
            @Query("newPassword") String newPassword
    );
}
