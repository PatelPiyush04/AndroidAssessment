package com.theherdonline.db.entity;

import androidx.room.ColumnInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResVersion {
    @SerializedName("android_version")
    @Expose
    private String android_version;

    @SerializedName("android_force_update")
    @Expose
    private Boolean android_force_update;



    public Integer getVersionNumber()
    {
        try {
            return Integer.valueOf(android_version);
        }
        catch (Exception e)
        {
            return 0;
        }
    }

    public String getAndroid_version() {
        return android_version;
    }

    public Boolean getAndroid_force_update() {
        return android_force_update;
    }
}
