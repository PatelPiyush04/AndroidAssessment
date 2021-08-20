package com.theherdonline.db.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResStream extends PagerData {

    @SerializedName("data")
    @Expose
    private List<StreamItem> data = null;


    public List<StreamItem> getData() {
        return data;
    }

    public void setData(List<StreamItem> data) {
        this.data = data;
    }
}
