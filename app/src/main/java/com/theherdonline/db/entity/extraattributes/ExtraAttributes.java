
package com.theherdonline.db.entity.extraattributes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExtraAttributes {

    @SerializedName("auctions_plus")
    @Expose
    private AuctionsPlus auctionsPlus;

    public AuctionsPlus getAuctionsPlus() {
        return auctionsPlus;
    }

    public void setAuctionsPlus(AuctionsPlus auctionsPlus) {
        this.auctionsPlus = auctionsPlus;
    }

}
