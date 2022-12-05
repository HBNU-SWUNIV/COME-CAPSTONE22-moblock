package com.example.hyperledgervirtualmoneyproject.DTO;

/*
{
    accessToken : Bearer eyJ0eXAiOiJBQ0NFU1NfVE9LRU4iLCJhbGciOiJIUzI1NiJ9
    .eyJzdWIiOiI1IiwiaWF0IjoxNjUzMTMxMDYyLCJleHAiOjE2NTMxMzQ2NjIsInJvbGUiOiJST0xFX1VTRVIifQ
    .ZUzqUXOfVkukkFfsXMKbi4iSTWZXjvs8KdQhAVBm3B4
}
 */

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserLoginDTO {
    private String accessToken;
}
