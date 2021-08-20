package com.theherdonline.db.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResPost extends PagerData {

    @SerializedName("data")
    @Expose
    private List<Post> data = null;


    public List<Post> getData() {
        return data;
    }

    public void setData(List<Post> data) {
        this.data = data;
    }
}