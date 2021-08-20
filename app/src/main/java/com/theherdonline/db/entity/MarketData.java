package com.theherdonline.db.entity;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "tableMarket")
public class MarketData {

    @PrimaryKey(autoGenerate = true)
    public int idd;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("marketing")
    @Expose
    @Embedded
    private Marketing marketing;

    @Override
    public String toString() {
        return "MarketData{" +
                "status='" + status + '\'' +
                ", marketing=" + marketing +
                '}';
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Marketing getMarketing() {
        return marketing;
    }

    public void setMarketing(Marketing marketing) {
        this.marketing = marketing;
    }

}

