package com.example.hyperledgervirtualmoneyproject.DTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserTradeResponseDTO {

    private String senderIdentifier;

    private String senderName;

    private String receiverIdentifier;

    private String receiverName;

    private String coinName;

    private Long amount;

    private String dateCreated;
}
