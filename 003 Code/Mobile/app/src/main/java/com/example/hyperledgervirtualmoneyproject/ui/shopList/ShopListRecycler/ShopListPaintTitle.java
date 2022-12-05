package com.example.hyperledgervirtualmoneyproject.ui.shopList.ShopListRecycler;

import android.graphics.Bitmap;

public class ShopListPaintTitle {

    public Bitmap img;
    public String name, phoneNumber, address;

    public ShopListPaintTitle(Bitmap img, String str, String str2, String str3){
        this.img = img;
        name = str;
        phoneNumber = str2;
        address = str3;
    }
}
