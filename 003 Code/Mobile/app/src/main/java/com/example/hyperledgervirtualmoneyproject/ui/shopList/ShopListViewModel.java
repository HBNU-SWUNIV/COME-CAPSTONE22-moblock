package com.example.hyperledgervirtualmoneyproject.ui.shopList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ShopListViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ShopListViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}