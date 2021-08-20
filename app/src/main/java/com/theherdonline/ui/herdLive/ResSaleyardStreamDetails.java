package com.theherdonline.ui.herdLive;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ResSaleyardStreamDetails extends Object implements Parcelable {

    public static final Creator<ResSaleyardStreamDetails> CREATOR = new Creator<ResSaleyardStreamDetails>() {
        @Override
        public ResSaleyardStreamDetails createFromParcel(Parcel in) {
            return new ResSaleyardStreamDetails(in);
        }

        @Override
        public ResSaleyardStreamDetails[] newArray(int size) {
            return new ResSaleyardStreamDetails[size];
        }
    };
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
    private Integer medialiveChannelId;
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
    @SerializedName("medialive_channel")
    @Expose
    private ResMedialive_channel medialiveChannel;
    @SerializedName("vods")
    @Expose
    private List<ResVods> mListVods;

    protected ResSaleyardStreamDetails(Parcel in) {

        if (in.readByte() == 0) {
            this.id = null;
        } else {
            this.id = in.readInt();
        }
        if (in.readByte() == 0) {
            this.userId = null;
        } else {
            this.userId = in.readInt();
        }
        this.name = in.readString();
        this.streamDate = in.readString();
        this.medialiveChannelId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
        this.deletedAt = in.readString();
        if (in.readByte() == 0) {
            this.isLive = null;
        } else {
            this.isLive = in.readInt();
        }
        this.toDeleteStreamAt = in.readString();

        this.medialiveChannel = in.readParcelable(ResMedialive_channel.class.getClassLoader());
        this.mListVods = new ArrayList<ResVods>();
        in.readList(this.mListVods, ResVods.class.getClassLoader());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResSaleyardStreamDetails saleyardStream = (ResSaleyardStreamDetails) o;
        return Objects.equals(id, saleyardStream.id) &&
                Objects.equals(userId, saleyardStream.userId) &&
                Objects.equals(name, saleyardStream.name) &&
                Objects.equals(streamDate, saleyardStream.streamDate) &&
                Objects.equals(medialiveChannelId, saleyardStream.medialiveChannelId) &&
                Objects.equals(createdAt, saleyardStream.createdAt) &&
                Objects.equals(updatedAt, saleyardStream.updatedAt) &&
                Objects.equals(deletedAt, saleyardStream.deletedAt) &&
                Objects.equals(isLive, saleyardStream.isLive) &&
                Objects.equals(toDeleteStreamAt, saleyardStream.toDeleteStreamAt) &&
                Objects.equals(medialiveChannel, saleyardStream.medialiveChannel) &&
                Objects.equals(mListVods, saleyardStream.mListVods);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, name, streamDate, medialiveChannelId, createdAt, updatedAt, deletedAt, isLive, toDeleteStreamAt, medialiveChannel, mListVods);
    }

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

    public Integer getMedialiveChannelId() {
        return medialiveChannelId;
    }

    public void setMedialiveChannelId(Integer medialiveChannelId) {
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

    public ResMedialive_channel getMedialiveChannel() {
        return medialiveChannel;
    }

    public void setMedialiveChannel(ResMedialive_channel medialiveChannel) {
        this.medialiveChannel = medialiveChannel;
    }

    public List<ResVods> getListVods() {
        return mListVods;
    }

    public void setListVods(List<ResVods> listVods) {
        mListVods = listVods;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        if (userId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(userId);
        }
        dest.writeString(name);
        dest.writeString(streamDate);
        dest.writeValue(medialiveChannelId);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeString(deletedAt);
        if (isLive == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(isLive);
        }
        dest.writeString(toDeleteStreamAt);
        dest.writeParcelable(this.medialiveChannel,flags);
        dest.writeList(this.mListVods);
    }
}
