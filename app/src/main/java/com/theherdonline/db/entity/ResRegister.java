package com.theherdonline.db.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResRegister {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("api_access_token")
    @Expose
    private String api_access_token;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("password")
    @Expose
    private String password;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApi_access_token() {
        return api_access_token;
    }

    public void setApi_access_token(String api_access_token) {
        this.api_access_token = api_access_token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
