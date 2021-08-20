package com.theherdonline.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


@Entity(tableName = "tableOrganisation")
public class Organisation implements Parcelable {

    @PrimaryKey
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("avatar")
    @Expose
    private String avatar;

    @SerializedName("biography")
    @Expose
    private String biography;

    @SerializedName("avatar_location")
    @Expose
    private String avatarLocation;

    @SerializedName("email")
    @Expose
    private String email;

/*
    @ColumnInfo(name = "org_admin_id")
    @SerializedName("admin_id")
    @Expose
    private Integer adminId;
*/

    @SerializedName("address_line_one")
    @Expose
    private String addressLineOne;

    @SerializedName("address_line_two")
    @Expose
    private String addressLineTwo;

    @SerializedName("address_suburb")
    @Expose
    private String addressSuburb;

    @SerializedName("address_city")
    @Expose
    private String addressCity;
    
    @SerializedName("address_postcode")
    @Expose
    private String addressPostcode;

    @SerializedName("users")
    @Expose
    private List<User> users;


/*
    @ColumnInfo(name = "org_lat")
    @SerializedName("lat")
    @Expose
    private Double lat;

    @ColumnInfo(name = "org_lng")
    @SerializedName("lng")
    @Expose
    private Double lng;
*/

    @SerializedName("facebook_url")
    @Expose
    private String facebookUrl;

    @SerializedName("instagram_url")
    @Expose
    private String instagramUrl;

    @SerializedName("twitter_url")
    @Expose
    private String twitterUrl;

    @SerializedName("youtube_url")
    @Expose
    private String youtubeUrl;


    @SerializedName("website_url")
    @Expose
    private String website_url;


    public String getWebsite_url() {
        return website_url;
    }

    public void setWebsite_url(String website_url) {
        this.website_url = website_url;
    }






/*

    @ColumnInfo(name = "org_prefer_invoice")
    @SerializedName("prefer_invoice")
    @Expose
    private Integer preferInvoice;
*/



    @ColumnInfo(name = "org_avatar_location_full_url")
    @SerializedName("avatar_location_full_url")
    @Expose
    private String avatar_location_full_url;




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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getAvatarLocation() {
        return avatarLocation;
    }

    public void setAvatarLocation(String avatarLocation) {
        this.avatarLocation = avatarLocation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddressLineOne() {
        return addressLineOne;
    }

    public void setAddressLineOne(String addressLineOne) {
        this.addressLineOne = addressLineOne;
    }

    public String getAddressLineTwo() {
        return addressLineTwo;
    }

    public void setAddressLineTwo(String addressLineTwo) {
        this.addressLineTwo = addressLineTwo;
    }

    public String getAddressSuburb() {
        return addressSuburb;
    }

    public void setAddressSuburb(String addressSuburb) {
        this.addressSuburb = addressSuburb;
    }

    public String getAddressCity() {
        return addressCity;
    }

    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    public String getAddressPostcode() {
        return addressPostcode;
    }

    public void setAddressPostcode(String addressPostcode) {
        this.addressPostcode = addressPostcode;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public String getInstagramUrl() {
        return instagramUrl;
    }

    public void setInstagramUrl(String instagramUrl) {
        this.instagramUrl = instagramUrl;
    }

    public String getTwitterUrl() {
        return twitterUrl;
    }

    public void setTwitterUrl(String twitterUrl) {
        this.twitterUrl = twitterUrl;
    }

    public String getYoutubeUrl() {
        return youtubeUrl;
    }

    public void setYoutubeUrl(String youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }

    public String getAvatar_location_full_url() {
        return avatar_location_full_url;
    }

    public void setAvatar_location_full_url(String avatar_location_full_url) {
        this.avatar_location_full_url = avatar_location_full_url;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Organisation() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeString(this.phone);
        dest.writeString(this.avatar);
        dest.writeString(this.biography);
        dest.writeString(this.avatarLocation);
        dest.writeString(this.email);
        dest.writeString(this.addressLineOne);
        dest.writeString(this.addressLineTwo);
        dest.writeString(this.addressSuburb);
        dest.writeString(this.addressCity);
        dest.writeString(this.addressPostcode);
        dest.writeTypedList(this.users);
        dest.writeString(this.facebookUrl);
        dest.writeString(this.instagramUrl);
        dest.writeString(this.twitterUrl);
        dest.writeString(this.youtubeUrl);
        dest.writeString(this.website_url);
        dest.writeString(this.avatar_location_full_url);
    }

    protected Organisation(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.phone = in.readString();
        this.avatar = in.readString();
        this.biography = in.readString();
        this.avatarLocation = in.readString();
        this.email = in.readString();
        this.addressLineOne = in.readString();
        this.addressLineTwo = in.readString();
        this.addressSuburb = in.readString();
        this.addressCity = in.readString();
        this.addressPostcode = in.readString();
        this.users = in.createTypedArrayList(User.CREATOR);
        this.facebookUrl = in.readString();
        this.instagramUrl = in.readString();
        this.twitterUrl = in.readString();
        this.youtubeUrl = in.readString();
        this.website_url = in.readString();
        this.avatar_location_full_url = in.readString();
    }

    public static final Creator<Organisation> CREATOR = new Creator<Organisation>() {
        @Override
        public Organisation createFromParcel(Parcel source) {
            return new Organisation(source);
        }

        @Override
        public Organisation[] newArray(int size) {
            return new Organisation[size];
        }
    };
}