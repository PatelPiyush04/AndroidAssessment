package com.theherdonline.ui.herdLive;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResHerdLive {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("user_id")
    @Expose
    private Integer userId;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("stream_date")
    @Expose
    private String streamDate;

    @SerializedName("medialive_channel_id")
    @Expose
    private String medialiveChannelId;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    @SerializedName("deleted_at")
    @Expose
    private String deletedAt;

    @SerializedName("is_live")
    @Expose
    private Integer isLive;

    @SerializedName("to_delete_stream_at")
    @Expose
    private String toDeleteStreamAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreamDate() {
        return streamDate;
    }

    public void setStreamDate(String streamDate) {
        this.streamDate = streamDate;
    }

    public String getMedialiveChannelId() {
        return medialiveChannelId;
    }

    public void setMedialiveChannelId(String medialiveChannelId) {
        this.medialiveChannelId = medialiveChannelId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Integer getIsLive() {
        return isLive;
    }

    public void setIsLive(Integer isLive) {
        this.isLive = isLive;
    }

    public String getToDeleteStreamAt() {
        return toDeleteStreamAt;
    }

    public void setToDeleteStreamAt(String toDeleteStreamAt) {
        this.toDeleteStreamAt = toDeleteStreamAt;
    }
}
