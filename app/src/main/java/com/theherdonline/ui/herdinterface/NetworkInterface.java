package com.theherdonline.ui.herdinterface;

public interface NetworkInterface {
    void onLoading(boolean bLoading);
    void onLoading(boolean bLoading, boolean bError);
    void onFailed(Integer code, String msg);
}
