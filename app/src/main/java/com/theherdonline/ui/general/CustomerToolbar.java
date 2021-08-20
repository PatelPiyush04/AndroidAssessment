package com.theherdonline.ui.general;

import android.view.View;

public class CustomerToolbar {
    public CustomerToolbar(Boolean mHasBackButton, String mTitle, Integer mImage1, View.OnClickListener mListenerImage1, Integer mImage2, View.OnClickListener mListenerImage2) {
        this.mHasBackButton = mHasBackButton;
        this.mTitle = mTitle;
        this.mImage1 = mImage1;
        this.mListenerImage1 = mListenerImage1;
        this.mImage2 = mImage2;
        this.mListenerImage2 = mListenerImage2;
    }


    public CustomerToolbar(String mTitle,View.OnClickListener backListener, Integer mImage1, View.OnClickListener mListenerImage1, Integer mImage2, View.OnClickListener mListenerImage2) {
        this.mHasBackButton = backListener == null ? false : true;
        this.mTitle = mTitle;
        this.mImage1 = mImage1;
        this.mListenerImage1 = mListenerImage1;
        this.mImage2 = mImage2;
        this.mListenerImage2 = mListenerImage2;
    }

    public CustomerToolbar(Boolean mHasBackButton, String mTitle,
                           String mCommand1, View.OnClickListener mCommandListener1,
                           Integer mImage0, View.OnClickListener mListenerImage0,
                           Integer mImage1, View.OnClickListener mListenerImage1,
                           Integer mImage2, View.OnClickListener mListenerImage2) {
        this.mHasBackButton = mHasBackButton;
        this.mTitle = mTitle;
        this.mCommand1 = mCommand1;
        this.mCommandListener1 = mCommandListener1;
        this.mImage0 = mImage0;
        this.mListenerImage0 = mListenerImage0;
        this.mImage1 = mImage1;
        this.mListenerImage1 = mListenerImage1;
        this.mImage2 = mImage2;
        this.mListenerImage2 = mListenerImage2;
    }

    public CustomerToolbar(Boolean mHasBackButton, String mTitle,
                           String mCommand1, View.OnClickListener mCommandListener1,
                           Integer mImageleft, View.OnClickListener mListenerImageleft,
                           Integer mImage0, View.OnClickListener mListenerImage0,
                           Integer mImage1, View.OnClickListener mListenerImage1,
                           Integer mImage2, View.OnClickListener mListenerImage2) {
        this.mHasBackButton = mHasBackButton;
        this.mTitle = mTitle;
        this.mCommand1 = mCommand1;
        this.mCommandListener1 = mCommandListener1;
        this.mImageLeft = mImageleft;
        this.mListenerImageLeft = mListenerImageleft;
        this.mImage0 = mImage0;
        this.mListenerImage0 = mListenerImage0;
        this.mImage1 = mImage1;
        this.mListenerImage1 = mListenerImage1;
        this.mImage2 = mImage2;
        this.mListenerImage2 = mListenerImage2;
    }


    public Boolean mHasBackButton;
    public String mTitle;
    public String mCommand1;
    public View.OnClickListener mCommandListener1;
    public Integer mImageLeft;
    public View.OnClickListener mListenerImageLeft;
    public Integer mImage0;
    public View.OnClickListener mListenerImage0;
    public Integer mImage1;
    public View.OnClickListener mListenerImage1;
    public Integer mImage2;
    public View.OnClickListener mListenerImage2;

    public Integer getmImageLeft() {
        return mImageLeft;
    }

    public void setmImageLeft(Integer mImageLeft) {
        this.mImageLeft = mImageLeft;
    }

    public View.OnClickListener getmListenerImageLeft() {
        return mListenerImageLeft;
    }

    public void setmListenerImageLeft(View.OnClickListener mListenerImageLeft) {
        this.mListenerImageLeft = mListenerImageLeft;
    }
}
