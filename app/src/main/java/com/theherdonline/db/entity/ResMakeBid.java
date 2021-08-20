package com.theherdonline.db.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResMakeBid {

    @SerializedName("amount")
    @Expose
    private String amount;

    @SerializedName("user_id")
    @Expose
    private Integer user_id;

    @SerializedName("bid_status_id")
    @Expose
    private Integer bid_status_id;

    @SerializedName("biddable_id")
    @Expose
    private Integer biddable_id;

    @SerializedName("id")
    @Expose
    private Integer id;

}
