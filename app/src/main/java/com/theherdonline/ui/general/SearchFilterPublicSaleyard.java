package com.theherdonline.ui.general;

import android.os.Parcel;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;

import org.joda.time.DateTime;

import java.util.Objects;

import com.theherdonline.db.entity.SaleyardCategory;
import com.theherdonline.util.ActivityUtils;

public class SearchFilterPublicSaleyard extends SearchFilter {

    public String  where_like;
    public LatLng  latLng;
    public Float   radius;
    public DateTime startDateTime;
    public DateTime endDateTime;
    public String name;
    public String type;
    public String description;
    public DateTime starts_at;
    public Integer saleyard_status_id;
    public SaleyardCategory saleyard_category;
    public Integer user_id;

    public Place mPlace;





    public Place getmPlace() {
        return mPlace;
    }

    public void setmPlace(Place mPlace) {
        this.mPlace = mPlace;
    }

    @Override
    public String getWhere_like() {
        return where_like;
    }

    @Override
    public void setWhere_like(String where_like) {
        this.where_like = where_like;
    }

    @Override
    public LatLng getLatLng() {
        return latLng;
    }

    @Override
    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    @Override
    public Float getRadius() {
        return radius;
    }

    @Override
    public void setRadius(Float radius) {
        this.radius = radius;
    }

    public DateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(DateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public DateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(DateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DateTime getStarts_at() {
        return starts_at;
    }

    public void setStarts_at(DateTime starts_at) {
        this.starts_at = starts_at;
    }

    public Integer getSaleyard_status_id() {
        return saleyard_status_id;
    }

    public void setSaleyard_status_id(Integer saleyard_status_id) {
        this.saleyard_status_id = saleyard_status_id;
    }

    public SaleyardCategory getSaleyard_category() {
        return saleyard_category;
    }

    public void setSaleyard_category(SaleyardCategory saleyard_category) {
        this.saleyard_category = saleyard_category;
    }

    @Override
    public Integer getUser_id() {
        return user_id;
    }

    @Override
    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SearchFilterPublicSaleyard)) return false;
        if (!super.equals(o)) return false;
        SearchFilterPublicSaleyard that = (SearchFilterPublicSaleyard) o;
        return Objects.equals(where_like, that.where_like) &&
                Objects.equals(latLng, that.latLng) &&
                Objects.equals(radius, that.radius) &&
                Objects.equals(startDateTime, that.startDateTime) &&
                Objects.equals(endDateTime, that.endDateTime) &&
                Objects.equals(name, that.name) &&
                Objects.equals(type, that.type) &&
                Objects.equals(description, that.description) &&
                Objects.equals(starts_at, that.starts_at) &&
                Objects.equals(saleyard_status_id, that.saleyard_status_id) &&
                Objects.equals(saleyard_category, that.saleyard_category) &&
                Objects.equals(user_id, that.user_id) &&
                ActivityUtils.EqualPlace(mPlace, that.mPlace);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), where_like, latLng, radius, startDateTime, endDateTime, name, type, description, starts_at, saleyard_status_id, saleyard_category, user_id, mPlace);
    }

    public SearchFilterPublicSaleyard() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.where_like);
        dest.writeParcelable(this.latLng, flags);
        dest.writeValue(this.radius);
        dest.writeSerializable(this.startDateTime);
        dest.writeSerializable(this.endDateTime);
        dest.writeString(this.name);
        dest.writeString(this.type);
        dest.writeString(this.description);
        dest.writeSerializable(this.starts_at);
        dest.writeValue(this.saleyard_status_id);
        dest.writeParcelable(this.saleyard_category, flags);
        dest.writeValue(this.user_id);
        dest.writeParcelable(this.mPlace, flags);
    }

    protected SearchFilterPublicSaleyard(Parcel in) {
        super(in);
        this.where_like = in.readString();
        this.latLng = in.readParcelable(LatLng.class.getClassLoader());
        this.radius = (Float) in.readValue(Float.class.getClassLoader());
        this.startDateTime = (DateTime) in.readSerializable();
        this.endDateTime = (DateTime) in.readSerializable();
        this.name = in.readString();
        this.type = in.readString();
        this.description = in.readString();
        this.starts_at = (DateTime) in.readSerializable();
        this.saleyard_status_id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.saleyard_category = in.readParcelable(SaleyardCategory.class.getClassLoader());
        this.user_id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.mPlace = in.readParcelable(Place.class.getClassLoader());
    }

    public static final Creator<SearchFilterPublicSaleyard> CREATOR = new Creator<SearchFilterPublicSaleyard>() {
        @Override
        public SearchFilterPublicSaleyard createFromParcel(Parcel source) {
            return new SearchFilterPublicSaleyard(source);
        }

        @Override
        public SearchFilterPublicSaleyard[] newArray(int size) {
            return new SearchFilterPublicSaleyard[size];
        }
    };
}
