package com.theherdonline.ui.general;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;

import com.theherdonline.db.entity.StreamType;

public class SearchFilter implements Parcelable {

    public String  where_like;
    public Double  lat;
    public Double  lng;
    public LatLng  latLng;
    public Float   radius;
    Integer producer_id;
    Integer agent_id;
    Integer organisation_id;
    Integer user_id;
    StreamType streamType;
    Integer of_user;





    public String getWhere_like() {
        return where_like;
    }

    public void setWhere_like(String where_like) {
        this.where_like = where_like;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public Float getRadius() {
        return radius;
    }

    public void setRadius(Float radius) {
        this.radius = radius;
    }

    public Integer getProducer_id() {
        return producer_id;
    }

    public void setProducer_id(Integer producer_id) {
        this.producer_id = producer_id;
    }

    public Integer getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(Integer agent_id) {
        this.agent_id = agent_id;
    }

    public Integer getOrganisation_id() {
        return organisation_id;
    }

    public void setOrganisation_id(Integer organisation_id) {
        this.organisation_id = organisation_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public StreamType getStreamType() {
        return streamType;
    }

    public void setStreamType(StreamType streamType) {
        this.streamType = streamType;
    }

    public Integer getOf_user() {
        return of_user;
    }

    public void setOf_user(Integer of_user) {
        this.of_user = of_user;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SearchFilter)) return false;
        SearchFilter that = (SearchFilter) o;
        return Objects.equals(where_like, that.where_like) &&
                Objects.equals(lat, that.lat) &&
                Objects.equals(lng, that.lng) &&
                Objects.equals(latLng, that.latLng) &&
                Objects.equals(radius, that.radius) &&
                Objects.equals(producer_id, that.producer_id) &&
                Objects.equals(agent_id, that.agent_id) &&
                Objects.equals(organisation_id, that.organisation_id) &&
                Objects.equals(user_id, that.user_id) &&
                streamType == that.streamType &&
                Objects.equals(of_user, that.of_user);
    }

    @Override
    public int hashCode() {

        return Objects.hash(where_like, lat, lng, latLng, radius, producer_id, agent_id, organisation_id, user_id, streamType, of_user);
    }

    public SearchFilter() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.where_like);
        dest.writeValue(this.lat);
        dest.writeValue(this.lng);
        dest.writeParcelable(this.latLng, flags);
        dest.writeValue(this.radius);
        dest.writeValue(this.producer_id);
        dest.writeValue(this.agent_id);
        dest.writeValue(this.organisation_id);
        dest.writeValue(this.user_id);
        dest.writeInt(this.streamType == null ? -1 : this.streamType.ordinal());
        dest.writeValue(this.of_user);
    }

    protected SearchFilter(Parcel in) {
        this.where_like = in.readString();
        this.lat = (Double) in.readValue(Double.class.getClassLoader());
        this.lng = (Double) in.readValue(Double.class.getClassLoader());
        this.latLng = in.readParcelable(LatLng.class.getClassLoader());
        this.radius = (Float) in.readValue(Float.class.getClassLoader());
        this.producer_id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.agent_id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.organisation_id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.user_id = (Integer) in.readValue(Integer.class.getClassLoader());
        int tmpStreamType = in.readInt();
        this.streamType = tmpStreamType == -1 ? null : StreamType.values()[tmpStreamType];
        this.of_user = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<SearchFilter> CREATOR = new Creator<SearchFilter>() {
        @Override
        public SearchFilter createFromParcel(Parcel source) {
            return new SearchFilter(source);
        }

        @Override
        public SearchFilter[] newArray(int size) {
            return new SearchFilter[size];
        }
    };
}

