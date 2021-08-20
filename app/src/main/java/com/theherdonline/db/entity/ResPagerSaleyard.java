package com.theherdonline.db.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.theherdonline.db.entity.EntitySaleyard;
import com.theherdonline.db.entity.PagerData;

public class ResPagerSaleyard extends PagerData {
    @SerializedName("data")
    @Expose
    private List<EntitySaleyard> data = null;


    public List<EntitySaleyard> getData() {
        return data;
    }

    public void setData(List<EntitySaleyard> data) {
        this.data = data;
    }
}
