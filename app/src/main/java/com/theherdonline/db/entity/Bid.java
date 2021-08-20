package com.theherdonline.db.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Bid {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("bid_status_id")
    @Expose
    private Integer bidStatusId;
    @SerializedName("biddable_type")
    @Expose
    private String biddableType;
    @SerializedName("biddable_id")
    @Expose
    private Integer biddableId;
    @SerializedName("amount")
    @Expose
    private Integer amount;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("dollar_amount")
    @Expose
    private String dollarAmount;
    @SerializedName("biddable")
    @Expose
    private Object biddable;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getBidStatusId() {
        return bidStatusId;
    }

    public void setBidStatusId(Integer bidStatusId) {
        this.bidStatusId = bidStatusId;
    }

    public String getBiddableType() {
        return biddableType;
    }

    public void setBiddableType(String biddableType) {
        this.biddableType = biddableType;
    }

    public Integer getBiddableId() {
        return biddableId;
    }

    public void setBiddableId(Integer biddableId) {
        this.biddableId = biddableId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDollarAmount() {
        return dollarAmount;
    }

    public void setDollarAmount(String dollarAmount) {
        this.dollarAmount = dollarAmount;
    }

    public Object getBiddable() {
        return biddable;
    }

    public void setBiddable(Object biddable) {
        this.biddable = biddable;
    }
}
