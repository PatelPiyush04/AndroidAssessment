
package com.theherdonline.db.entity.extraattributes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Description {

    @SerializedName("breed")
    @Expose
    private String breed;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("agencyTradingName")
    @Expose
    private String agencyTradingName;

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAgencyTradingName() {
        return agencyTradingName;
    }

    public void setAgencyTradingName(String agencyTradingName) {
        this.agencyTradingName = agencyTradingName;
    }

}
