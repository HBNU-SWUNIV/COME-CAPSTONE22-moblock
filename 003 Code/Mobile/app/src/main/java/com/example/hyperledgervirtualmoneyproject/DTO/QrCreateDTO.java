package com.example.hyperledgervirtualmoneyproject.DTO;

import java.util.Objects;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class QrCreateDTO {
    private Long receiverId;
    private String coinName;
    private Long amount;
}
