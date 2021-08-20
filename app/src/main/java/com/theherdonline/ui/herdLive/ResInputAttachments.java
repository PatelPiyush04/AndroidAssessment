package com.theherdonline.ui.herdLive;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResInputAttachments {

    @SerializedName("InputId")
    @Expose
    private String InputId;

    public String getInputId() {
        return InputId;
    }

    public void setInputId(String inputId) {
        InputId = inputId;
    }
}
