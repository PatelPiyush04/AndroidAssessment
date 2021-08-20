
package com.theherdonline.db.entity.extraattributes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Accreditation {

    @SerializedName("accreditationNumber")
    @Expose
    private Object accreditationNumber;
    @SerializedName("accreditationLabel")
    @Expose
    private String accreditationLabel;
    @SerializedName("accreditationNumberLabel")
    @Expose
    private String accreditationNumberLabel;
    @SerializedName("description")
    @Expose
    private String description;

    public Object getAccreditationNumber() {
        return accreditationNumber;
    }

    public void setAccreditationNumber(Object accreditationNumber) {
        this.accreditationNumber = accreditationNumber;
    }

    public String getAccreditationLabel() {
        return accreditationLabel;
    }

    public void setAccreditationLabel(String accreditationLabel) {
        this.accreditationLabel = accreditationLabel;
    }

    public String getAccreditationNumberLabel() {
        return accreditationNumberLabel;
    }

    public void setAccreditationNumberLabel(String accreditationNumberLabel) {
        this.accreditationNumberLabel = accreditationNumberLabel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
