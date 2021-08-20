package com.theherdonline.db.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Media implements Parcelable {


    public static final Creator<Media> CREATOR = new Creator<Media>() {
        @Override
        public Media createFromParcel(Parcel source) {
            return new Media(source);
        }

        @Override
        public Media[] newArray(int size) {
            return new Media[size];
        }
    };
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("attachments")
    @Expose
    private String url_attchment;
    private Boolean isPrimary = false;
    @SerializedName("collection_name")
    @Expose
    private String collection_name;
    public Media() {
    }


    protected Media(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.url = in.readString();
        this.isPrimary = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.collection_name = in.readString();
    }

    public String getUrl_attchment() {
        return url_attchment;
    }

    public void setUrl_attchment(String url_attchment) {
        this.url_attchment = url_attchment;
    }

    public String getCollection_name() {

        return collection_name;
    }

    public void setCollection_name(String collection_name) {
        this.collection_name = collection_name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Boolean getPrimary() {
        return isPrimary;
    }

    public void setPrimary(Boolean primary) {
        isPrimary = primary;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeString(this.url);
        dest.writeValue(this.isPrimary);
        dest.writeString(this.collection_name);
        dest.writeString(this.url_attchment);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Media)) return false;
        Media media = (Media) o;
        return Objects.equals(id, media.id) &&
                Objects.equals(name, media.name) &&
                Objects.equals(url, media.url) &&
                Objects.equals(isPrimary, media.isPrimary) &&
                Objects.equals(collection_name, media.collection_name) &&
                Objects.equals(url_attchment, media.url_attchment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, url, isPrimary, collection_name, url_attchment);
    }

    public enum CoolectionName {
        pdfs, images, videos, others
    }
}
