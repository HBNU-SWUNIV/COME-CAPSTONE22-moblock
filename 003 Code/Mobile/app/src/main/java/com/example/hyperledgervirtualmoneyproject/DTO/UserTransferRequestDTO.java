package com.example.hyperledgervirtualmoneyproject.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public class UserTransferRequestDTO {
    private String receiverIdentifier;

    private String coinName;

    private Long amount;
}
