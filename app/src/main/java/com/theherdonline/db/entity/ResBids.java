package com.theherdonline.db.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;




public class ResBids extends PagerData {

    @SerializedName("data")
    @Expose
    private List<Bid> data = null;


    public List<Bid> getData() {
        return data;
    }

    public void setData(List<Bid> data) {
        this.data = data;
    }
}