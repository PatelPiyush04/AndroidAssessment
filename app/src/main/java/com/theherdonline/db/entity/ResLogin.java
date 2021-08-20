package com.theherdonline.db.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResLogin {

    @SerializedName("token_type")
    @Expose
    private String status;

    @SerializedName("expires_in")
    @Expose
    private String expires_in;

    @SerializedName("access_token")
    @Expose
    private String access_token;

    @SerializedName("refresh_token")
    @Expose
    private String refresh_token;

    @SerializedName("addition_for_test")
    @Expose
    private String addition_for_test;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getAddition_for_test() {
        return addition_for_test;
    }

    public void setAddition_for_test(String addition_for_test) {
        this.addition_for_test = addition_for_test;
    }
}
