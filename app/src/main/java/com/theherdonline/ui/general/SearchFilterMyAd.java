package com.theherdonline.ui.general;

import android.os.Parcel;

import java.util.Objects;

import com.theherdonline.db.entity.StreamType;

public class SearchFilterMyAd extends SearchFilter {


    public Integer producer_id;
    public Integer agent_id;
    public Integer organisation_id;
    public Integer user_id;
    public StreamType streamType;
    public Integer of_user;
    public ADType adType;




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SearchFilterMyAd)) return false;
        if (!super.equals(o)) return false;
        SearchFilterMyAd that = (SearchFilterMyAd) o;
        return Objects.equals(producer_id, that.producer_id) &&
                Objects.equals(agent_id, that.agent_id) &&
                Objects.equals(organisation_id, that.organisation_id) &&
                Objects.equals(user_id, that.user_id) &&
                streamType == that.streamType &&
                Objects.equals(of_user, that.of_user) &&
                adType == that.adType;
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), producer_id, agent_id, organisation_id, user_id, streamType, of_user, adType);
    }

    @Override
    public Integer getProducer_id() {
        return producer_id;
    }

    @Override
    public void setProducer_id(Integer producer_id) {
        this.producer_id = producer_id;
    }

    @Override
    public Integer getAgent_id() {
        return agent_id;
    }

    @Override
    public void setAgent_id(Integer agent_id) {
        this.agent_id = agent_id;
    }

    @Override
    public Integer getOrganisation_id() {
        return organisation_id;
    }

    @Override
    public void setOrganisation_id(Integer organisation_id) {
        this.organisation_id = organisation_id;
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
    public StreamType getStreamType() {
        return streamType;
    }

    @Override
    public void setStreamType(StreamType streamType) {
        this.streamType = streamType;
    }

    @Override
    public Integer getOf_user() {
        return of_user;
    }

    @Override
    public void setOf_user(Integer of_user) {
        this.of_user = of_user;
    }

    public ADType getAdType() {
        return adType;
    }

    public void setAdType(ADType adType) {
        this.adType = adType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this.producer_id);
        dest.writeValue(this.agent_id);
        dest.writeValue(this.organisation_id);
        dest.writeValue(this.user_id);
        dest.writeInt(this.streamType == null ? -1 : this.streamType.ordinal());
        dest.writeValue(this.of_user);
        dest.writeInt(this.adType == null ? -1 : this.adType.ordinal());
    }

    public SearchFilterMyAd() {
    }

    protected SearchFilterMyAd(Parcel in) {
        super(in);
        this.producer_id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.agent_id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.organisation_id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.user_id = (Integer) in.readValue(Integer.class.getClassLoader());
        int tmpStreamType = in.readInt();
        this.streamType = tmpStreamType == -1 ? null : StreamType.values()[tmpStreamType];
        this.of_user = (Integer) in.readValue(Integer.class.getClassLoader());
        int tmpAdType = in.readInt();
        this.adType = tmpAdType == -1 ? null : ADType.values()[tmpAdType];
    }

    public static final Creator<SearchFilterMyAd> CREATOR = new Creator<SearchFilterMyAd>() {
        @Override
        public SearchFilterMyAd createFromParcel(Parcel source) {
            return new SearchFilterMyAd(source);
        }

        @Override
        public SearchFilterMyAd[] newArray(int size) {
            return new SearchFilterMyAd[size];
        }
    };
}

