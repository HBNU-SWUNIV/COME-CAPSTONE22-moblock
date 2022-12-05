package com.example.hyperledgervirtualmoneyproject.ui.home.CoinListView;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoinData {
    private String coinName;
    private String coinValue;

    public CoinData(String coinName, String coinValue){
        this.coinName = coinName;
        this.coinValue = coinValue;
    }
}