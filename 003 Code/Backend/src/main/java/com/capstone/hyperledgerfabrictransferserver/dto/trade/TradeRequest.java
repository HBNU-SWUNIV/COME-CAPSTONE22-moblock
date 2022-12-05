package com.capstone.hyperledgerfabrictransferserver.dto.trade;

import com.sun.istack.NotNull;
import io.swagger.annotations.ApiParam;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class TradeRequest {

    @ApiParam(required = true)
    @NotNull
    private String receiverIdentifier;

    @ApiParam(required = true)
    @NotNull
    private String coinName;

    @ApiParam(required = true)
    @NotNull
    private Long amount;

    @Builder
    public TradeRequest(String receiverIdentifier, String coinName, Long amount) {
        this.receiverIdentifier = receiverIdentifier;
        this.coinName = coinName;
        this.amount = amount;
    }
}
