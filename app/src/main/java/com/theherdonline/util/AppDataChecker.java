package com.theherdonline.util;

import android.content.Context;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AppDataChecker {
    private Context mContex;

    @Inject
    public AppDataChecker(Context mContex) {
        this.mContex = mContex;
    }




}
