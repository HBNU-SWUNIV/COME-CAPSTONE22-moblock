package com.example.hyperledgervirtualmoneyproject;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

public class LoadingDialog extends Dialog {
    public LoadingDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_loading);
    }

}