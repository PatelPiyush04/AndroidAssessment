package com.theherdonline.ui.herdLive;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResDestinations {

    @SerializedName("Ip")
    @Expose
    private String Ip;

    public String getIp() {
        return Ip;
    }

    public void setIp(String ip) {
        Ip = ip;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    @SerializedName("Url")
    @Expose
    private String Url;

}
