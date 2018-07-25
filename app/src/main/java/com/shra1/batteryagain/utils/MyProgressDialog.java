package com.shra1.batteryagain.utils;

import android.app.ProgressDialog;
import android.content.Context;

public class MyProgressDialog {
    static ProgressDialog progressDialog;

    public static void show(Context mCtx, String text, boolean isCancleable) {
        progressDialog = new ProgressDialog(mCtx);
        progressDialog.setMessage(text);
        progressDialog.setCancelable(isCancleable);
        progressDialog.show();
    }


    public static void dismiss() {
        progressDialog.dismiss();
    }
}
