package com.theherdonline.ui.herdLive;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class ResMedialive_channel implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("user_id")
    @Expose
    private Integer userId;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    @SerializedName("deleted_at")
    @Expose
    private String deletedAt;

    @SerializedName("aws_name")
    @Expose
    private String awsName;

    @SerializedName("aws_medialive_id")
    @Expose
    private Integer awsMedialiveId;

    @SerializedName("aws_medialive_push_endpoint")
    @Expose
    private String awsMedialivePushEndpoint;

    @SerializedName("aws_livestream_viewing_url")
    @Expose
    private String awsLivestreamViewingUrl;

    protected ResMedialive_channel(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        if (in.readByte() == 0) {
            userId = null;
        } else {
            userId = in.readInt();
        }
        createdAt = in.readString();
        updatedAt = in.readString();
        deletedAt = in.readString();
        awsName = in.readString();
        awsMedialiveId = (Integer) in.readValue(Integer.class.getClassLoader());
        awsMedialivePushEndpoint = in.readString();
        awsLivestreamViewingUrl = in.readString();
    }

    public static final Creator<ResMedialive_channel> CREATOR = new Creator<ResMedialive_channel>() {
        @Override
        public ResMedialive_channel createFromParcel(Parcel in) {
            return new ResMedialive_channel(in);
        }

        @Override
        public ResMedialive_channel[] newArray(int size) {
            return new ResMedialive_channel[size];
        }
    };

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

    public String getAwsName() {
        return awsName;
    }

    public void setAwsName(String awsName) {
        this.awsName = awsName;
    }

    public Integer getAwsMedialiveId() {
        return awsMedialiveId;
    }

    public void setAwsMedialiveId(Integer awsMedialiveId) {
        this.awsMedialiveId = awsMedialiveId;
    }

    public String getAwsMedialivePushEndpoint() {
        return awsMedialivePushEndpoint;
    }

    public void setAwsMedialivePushEndpoint(String awsMedialivePushEndpoint) {
        this.awsMedialivePushEndpoint = awsMedialivePushEndpoint;
    }

    public String getAwsLivestreamViewingUrl() {
        return awsLivestreamViewingUrl;
    }

    public void setAwsLivestreamViewingUrl(String awsLivestreamViewingUrl) {
        this.awsLivestreamViewingUrl = awsLivestreamViewingUrl;
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
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeString(deletedAt);
        dest.writeString(awsName);
        dest.writeValue(awsMedialiveId);
        dest.writeString(awsMedialivePushEndpoint);
        dest.writeString(awsLivestreamViewingUrl);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResMedialive_channel that = (ResMedialive_channel) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(updatedAt, that.updatedAt) &&
                Objects.equals(deletedAt, that.deletedAt) &&
                Objects.equals(awsName, that.awsName) &&
                Objects.equals(awsMedialiveId, that.awsMedialiveId) &&
                Objects.equals(awsMedialivePushEndpoint, that.awsMedialivePushEndpoint) &&
                Objects.equals(awsLivestreamViewingUrl, that.awsLivestreamViewingUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, createdAt, updatedAt, deletedAt, awsName, awsMedialiveId, awsMedialivePushEndpoint, awsLivestreamViewingUrl);
    }
}
