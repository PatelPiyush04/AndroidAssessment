package com.theherdonline.ui.herdLive;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResAWSMedialiveChannel {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("State")
    @Expose
    private String State;

    public List<ResInputAttachments> getInputAttachments() {
        return InputAttachments;
    }

    public void setInputAttachments(List<ResInputAttachments> inputAttachments) {
        InputAttachments = inputAttachments;
    }

    @SerializedName("InputAttachments")
    @Expose
    private List<ResInputAttachments> InputAttachments;

    @SerializedName("Destinations")
    @Expose
    private List<ResDestinations> Destinations;

    public List<ResDestinations> getDestinations() {
        return Destinations;
    }

    public void setDestinations(List<ResDestinations> destinations) {
        Destinations = destinations;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }
}
