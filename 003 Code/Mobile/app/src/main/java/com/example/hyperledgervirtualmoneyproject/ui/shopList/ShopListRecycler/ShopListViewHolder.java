package com.example.hyperledgervirtualmoneyproject.ui.shopList.ShopListRecycler;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.hyperledgervirtualmoneyproject.R;

public class ShopListViewHolder extends RecyclerView.ViewHolder {

    ImageView shopImg;
    TextView name, phoneNumber, address;

    public ShopListViewHolder(View itemView){
        super(itemView);
        shopImg = (ImageView) itemView.findViewById(R.id.shopImg);
        name = (TextView) itemView.findViewById(R.id.shopName);
        phoneNumber = (TextView) itemView.findViewById(R.id.shopPhoneNumber);
        address = (TextView) itemView.findViewById(R.id.shopAddress);
    }
}
