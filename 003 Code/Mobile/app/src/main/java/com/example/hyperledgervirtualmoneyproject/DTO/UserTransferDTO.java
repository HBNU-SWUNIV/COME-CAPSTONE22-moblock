package com.example.hyperledgervirtualmoneyproject.DTO;


import lombok.ToString;

@ToString
public class UserTransferDTO {

    private Long senderIdentifier;

    private String senderName;

    private Long receiverIdentifier;

    private String receiverName;

    private String coinName;

    private Long amount;

    private String dateCreated;
}
