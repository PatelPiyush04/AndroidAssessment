package com.theherdonline.db.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResMarketReport extends PagerData {
    @SerializedName("data")
    @Expose
    private List<MarketReport> data = null;


    public List<MarketReport> getData() {
        return data;
    }

    public void setData(List<MarketReport> data) {
        this.data = data;
    }
}
