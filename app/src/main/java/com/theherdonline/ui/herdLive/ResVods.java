package com.theherdonline.ui.herdLive;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class ResVods implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("view_count")
    @Expose
    private String viewCount;

    @SerializedName("date_recorded")
    @Expose
    private String dateRecorded;

    @SerializedName("saleyard_stream_id")
    @Expose
    private String saleyardStreamId;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    @SerializedName("deleted_at")
    @Expose
    private String deletedAt;

    protected ResVods(Parcel in) {
        id = in.readString();
        name = in.readString();
        url = in.readString();
        viewCount = in.readString();
        dateRecorded = in.readString();
        saleyardStreamId = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        deletedAt = in.readString();
    }

    public static final Creator<ResVods> CREATOR = new Creator<ResVods>() {
        @Override
        public ResVods createFromParcel(Parcel in) {
            return new ResVods(in);
        }

        @Override
        public ResVods[] newArray(int size) {
            return new ResVods[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getViewCount() {
        return viewCount;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }

    public String getDateRecorded() {
        return dateRecorded;
    }

    public void setDateRecorded(String dateRecorded) {
        this.dateRecorded = dateRecorded;
    }

    public String getSaleyardStreamId() {
        return saleyardStreamId;
    }

    public void setSaleyardStreamId(String saleyardStreamId) {
        this.saleyardStreamId = saleyardStreamId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResVods resVods = (ResVods) o;
        return Objects.equals(id, resVods.id) &&
                Objects.equals(name, resVods.name) &&
                Objects.equals(url, resVods.url) &&
                Objects.equals(viewCount, resVods.viewCount) &&
                Objects.equals(dateRecorded, resVods.dateRecorded) &&
                Objects.equals(saleyardStreamId, resVods.saleyardStreamId) &&
                Objects.equals(createdAt, resVods.createdAt) &&
                Objects.equals(updatedAt, resVods.updatedAt) &&
                Objects.equals(deletedAt, resVods.deletedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, url, viewCount, dateRecorded, saleyardStreamId, createdAt, updatedAt, deletedAt);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(url);
        dest.writeString(viewCount);
        dest.writeString(dateRecorded);
        dest.writeString(saleyardStreamId);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeString(deletedAt);
    }
}
