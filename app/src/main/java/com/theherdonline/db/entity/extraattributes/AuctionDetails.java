
package com.theherdonline.db.entity.extraattributes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuctionDetails {

    @SerializedName("auctionNumber")
    @Expose
    private String auctionNumber;
    @SerializedName("auctionDescription")
    @Expose
    private String auctionDescription;
    @SerializedName("dateTime")
    @Expose
    private String dateTime;
    @SerializedName("lotNumber")
    @Expose
    private Object lotNumber;
    @SerializedName("auctionStatus")
    @Expose
    private String auctionStatus;
    @SerializedName("auctionLink")
    @Expose
    private String auctionLink;

    public String getAuctionNumber() {
        return auctionNumber;
    }

    public void setAuctionNumber(String auctionNumber) {
        this.auctionNumber = auctionNumber;
    }

    public String getAuctionDescription() {
        return auctionDescription;
    }

    public void setAuctionDescription(String auctionDescription) {
        this.auctionDescription = auctionDescription;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Object getLotNumber() {
        return lotNumber;
    }

    public void setLotNumber(Object lotNumber) {
        this.lotNumber = lotNumber;
    }

    public String getAuctionStatus() {
        return auctionStatus;
    }

    public void setAuctionStatus(String auctionStatus) {
        this.auctionStatus = auctionStatus;
    }

    public String getAuctionLink() {
        return auctionLink;
    }

    public void setAuctionLink(String auctionLink) {
        this.auctionLink = auctionLink;
    }

}
