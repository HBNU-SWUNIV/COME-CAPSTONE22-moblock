package com.example.hyperledgervirtualmoneyproject.DTO;



//"code": "400",
//    "error type": "Bad Request",
//    "message": "잘못된 식별 번호로 요청했습니다"

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public class ErrorBody {
    private String code;

    @JsonProperty("error type")
    private String errorType;

    private String message;
}
