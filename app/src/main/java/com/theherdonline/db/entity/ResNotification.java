package com.theherdonline.db.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResNotification extends PagerData  {
    @SerializedName("data")
    @Expose
    private List<HerdNotification> data = null;


    public List<HerdNotification> getData() {
        return data;
    }

    public void setData(List<HerdNotification> data) {
        this.data = data;
    }
}
