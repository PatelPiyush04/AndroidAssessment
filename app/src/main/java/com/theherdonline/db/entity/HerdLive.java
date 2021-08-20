package com.theherdonline.db.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HerdLive implements Parcelable {


    public static final Creator<HerdLive> CREATOR = new Creator<HerdLive>() {
        @Override
        public HerdLive createFromParcel(Parcel in) {
            return new HerdLive(in);
        }

        @Override
        public HerdLive[] newArray(int size) {
            return new HerdLive[size];
        }
    };
    @SerializedName("id")
    @Expose
    private final Integer id;
    @SerializedName("user_id")
    @Expose
    private final Integer userId;
    @SerializedName("name")
    @Expose
    private final String name;
    @SerializedName("stream_date")
    @Expose
    private final String streamDate;
    @SerializedName("medialive_channel_id")
    @Expose
    private final String medialiveChannelId;
    @SerializedName("created_at")
    @Expose
    private final String createdAt;
    @SerializedName("updated_at")
    @Expose
    private final String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    private final String deletedAt;
    @SerializedName("is_live")
    @Expose
    private final Integer isLive;

    public Integer getId() {
        return id;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getStreamDate() {
        return streamDate;
    }

    public String getMedialiveChannelId() {
        return medialiveChannelId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public Integer getIsLive() {
        return isLive;
    }

    public String getToDeleteStreamAt() {
        return toDeleteStreamAt;
    }

    @SerializedName("to_delete_stream_at")
    @Expose
    private final String toDeleteStreamAt;


    protected HerdLive(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.userId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.streamDate = in.readString();
        this.medialiveChannelId = in.readString();
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
        this.deletedAt = in.readString();
        this.isLive = (Integer) in.readValue(Integer.class.getClassLoader());
        this.toDeleteStreamAt = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeValue(this.userId);
        dest.writeValue(this.name);
        dest.writeValue(this.streamDate);
        dest.writeValue(this.medialiveChannelId);
        dest.writeValue(this.createdAt);
        dest.writeValue(this.updatedAt);
        dest.writeValue(this.deletedAt);
        dest.writeValue(this.isLive);
        dest.writeValue(this.toDeleteStreamAt);
    }
}
