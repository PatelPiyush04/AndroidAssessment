package com.theherdonline.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

@Entity(tableName = "tableAgent")
public class Agent implements Parcelable {

    @PrimaryKey
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("first_name")
    @Expose
    private String firstName;

    @SerializedName("last_name")
    @Expose
    private String last_name;

    @SerializedName("full_name")
    @Expose
    private String full_name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    @Override
    public String toString() {
        return  firstName + " " + last_name;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Agent)) return false;
        Agent agent = (Agent) o;
        return Objects.equals(id, agent.id) &&
                Objects.equals(firstName, agent.firstName) &&
                Objects.equals(last_name, agent.last_name) &&
                Objects.equals(full_name, agent.full_name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, firstName, last_name, full_name);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.firstName);
        dest.writeString(this.last_name);
        dest.writeString(this.full_name);
    }

    public Agent() {
    }

    protected Agent(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.firstName = in.readString();
        this.last_name = in.readString();
        this.full_name = in.readString();
    }

    public static final Parcelable.Creator<Agent> CREATOR = new Parcelable.Creator<Agent>() {
        @Override
        public Agent createFromParcel(Parcel source) {
            return new Agent(source);
        }

        @Override
        public Agent[] newArray(int size) {
            return new Agent[size];
        }
    };
}
