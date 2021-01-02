package com.loadease.uberclone.adminpanels.Messages;

import android.content.Context;

public class messageBlockorVerify {
    Context context;
    String chk="";

    public messageBlockorVerify(Context context, String chk) {
        this.context = context;
        this.chk = chk;
        if (chk.equals("B")){
            ProessBlock();
        }else {
            Proessverifynoti();
            
        }
    }

    private void Proessverifynoti() {
    }

    private void ProessBlock() {
    }
}
