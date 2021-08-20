
package com.theherdonline.db.entity.extraattributes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Breed {

    @SerializedName("dam")
    @Expose
    private String dam;
    @SerializedName("sire")
    @Expose
    private String sire;
    @SerializedName("numberOfHead")
    @Expose
    private Integer numberOfHead;
    @SerializedName("type")
    @Expose
    private Object type;
    @SerializedName("percentage")
    @Expose
    private Object percentage;

    public String getDam() {
        return dam;
    }

    public void setDam(String dam) {
        this.dam = dam;
    }

    public String getSire() {
        return sire;
    }

    public void setSire(String sire) {
        this.sire = sire;
    }

    public Integer getNumberOfHead() {
        return numberOfHead;
    }

    public void setNumberOfHead(Integer numberOfHead) {
        this.numberOfHead = numberOfHead;
    }

    public Object getType() {
        return type;
    }

    public void setType(Object type) {
        this.type = type;
    }

    public Object getPercentage() {
        return percentage;
    }

    public void setPercentage(Object percentage) {
        this.percentage = percentage;
    }

}
