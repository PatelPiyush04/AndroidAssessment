package com.theherdonline.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;


@Entity(tableName = "tableChat")
public class Chat implements Parcelable {

    @PrimaryKey
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("user_id")
    @Expose
    private Integer user_id;

    @SerializedName("agent_id")
    @Expose
    private Integer agent_id;

    @SerializedName("livestock_id")
    @Expose
    private Integer livestock_id;

    @SerializedName("room_id")
    @Expose
    private Integer room_id;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("updated_at")
    @Expose
    private String updated_at;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(Integer agent_id) {
        this.agent_id = agent_id;
    }

    public Integer getLivestock_id() {
        return livestock_id;
    }

    public void setLivestock_id(Integer livestock_id) {
        this.livestock_id = livestock_id;
    }

    public Integer getRoom_id() {
        return room_id;
    }

    public void setRoom_id(Integer room_id) {
        this.room_id = room_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Chat)) return false;
        Chat chat = (Chat) o;
        return Objects.equals(id, chat.id) &&
                Objects.equals(user_id, chat.user_id) &&
                Objects.equals(agent_id, chat.agent_id) &&
                Objects.equals(livestock_id, chat.livestock_id) &&
                Objects.equals(room_id, chat.room_id) &&
                Objects.equals(created_at, chat.created_at) &&
                Objects.equals(updated_at, chat.updated_at);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, user_id, agent_id, livestock_id, room_id, created_at, updated_at);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeValue(this.user_id);
        dest.writeValue(this.agent_id);
        dest.writeValue(this.livestock_id);
        dest.writeValue(this.room_id);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
    }

    public Chat() {
    }

    protected Chat(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.user_id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.agent_id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.livestock_id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.room_id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.created_at = in.readString();
        this.updated_at = in.readString();
    }

    public static final Parcelable.Creator<Chat> CREATOR = new Parcelable.Creator<Chat>() {
        @Override
        public Chat createFromParcel(Parcel source) {
            return new Chat(source);
        }

        @Override
        public Chat[] newArray(int size) {
            return new Chat[size];
        }
    };
}
