package com.theherdonline.db.entity;

import androidx.room.Ignore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Marketing {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("gym_id")
    @Expose
    private Integer gymId;
    @SerializedName("time_on")
    @Expose
    private Integer timeOn;
    @SerializedName("time_off")
    @Expose
    private Integer timeOff;
    @SerializedName("lobby")
    @Expose
    private Integer lobby;
    @SerializedName("arena1")
    @Expose
    private Integer arena1;
    @SerializedName("arena2")
    @Expose
    private Integer arena2;



    @SerializedName("assets")
    @Expose
    @Ignore
    private List<String> assets = null;


    private String assetsList = null;

    public void setAssetsList(String assetsList) {
        this.assetsList = assetsList;
    }

    public String getAssetsList() {
        if (assets == null)
            return "";
        assetsList = "";
        for (String s : assets)
        {
            assetsList += "," + s;
        }

        return assetsList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGymId() {
        return gymId;
    }

    public void setGymId(Integer gymId) {
        this.gymId = gymId;
    }

    public Integer getTimeOn() {
        return timeOn;
    }

    public void setTimeOn(Integer timeOn) {
        this.timeOn = timeOn;
    }

    public Integer getTimeOff() {
        return timeOff;
    }

    public void setTimeOff(Integer timeOff) {
        this.timeOff = timeOff;
    }

    public Integer getLobby() {
        return lobby;
    }

    public void setLobby(Integer lobby) {
        this.lobby = lobby;
    }

    public Integer getArena1() {
        return arena1;
    }

    public void setArena1(Integer arena1) {
        this.arena1 = arena1;
    }

    public Integer getArena2() {
        return arena2;
    }

    public void setArena2(Integer arena2) {
        this.arena2 = arena2;
    }

    public List<String> getAssets() {
        return assets;
    }

    public void setAssets(List<String> assets) {
        this.assets = assets;
    }
}
