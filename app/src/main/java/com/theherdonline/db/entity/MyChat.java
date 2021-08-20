package com.theherdonline.db.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;


@Entity(tableName = "tableHerdChat")
public class MyChat implements Parcelable {

    @PrimaryKey
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("user_id")
    @Expose
    private Integer user_id;

    @SerializedName("livestock_id")
    @Expose
    private Integer livestock_id;


    @SerializedName("room_id")
    @Expose
    private String room_id;


    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("updated_at")
    @Expose
    private String updated_at;

    @SerializedName("agent_id")
    @Expose
    private Integer agent_id;

    @SerializedName("user_unread")
    @Expose
    private Boolean user_unread;

    @SerializedName("agent_unread")
    @Expose
    private Boolean agent_unread;



    @Ignore
    @SerializedName("user")
    @Expose
    private User user;

    @Ignore
    @SerializedName("agent")
    @Expose
    private User agent;

    @Ignore
    @SerializedName("livestock")
    @Expose
    private EntityLivestock livestock;

    private String agent_avatar_location;
    private String agent_name;
    private String user_avatar_location;
    private String user_name;
    private String livestockName;


    public void setupExtraData()
    {
        agent_avatar_location = agent.getAvatarLocation();
        agent_name = agent.getFullName();
        user_avatar_location = user.getAvatarLocation();
        user_name = user.getFullName();
        livestockName = livestock.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MyChat)) return false;
        MyChat myChat = (MyChat) o;
        return Objects.equals(id, myChat.id) &&
                Objects.equals(user_id, myChat.user_id) &&
                Objects.equals(livestock_id, myChat.livestock_id) &&
                Objects.equals(room_id, myChat.room_id) &&
                Objects.equals(created_at, myChat.created_at) &&
                Objects.equals(updated_at, myChat.updated_at) &&
                Objects.equals(agent_id, myChat.agent_id) &&
                Objects.equals(user_unread, myChat.user_unread) &&
                Objects.equals(agent_unread, myChat.agent_unread) &&
                Objects.equals(agent_avatar_location, myChat.agent_avatar_location) &&
                Objects.equals(agent_name, myChat.agent_name) &&
                Objects.equals(user_avatar_location, myChat.user_avatar_location) &&
                Objects.equals(user_name, myChat.user_name) &&
                Objects.equals(livestockName, myChat.livestockName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, user_id, livestock_id, room_id, created_at, updated_at, agent_id, user_unread, agent_unread, agent_avatar_location, agent_name, user_avatar_location, user_name, livestockName);
    }

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

    public Integer getLivestock_id() {
        return livestock_id;
    }

    public void setLivestock_id(Integer livestock_id) {
        this.livestock_id = livestock_id;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
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

    public Integer getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(Integer agent_id) {
        this.agent_id = agent_id;
    }

    public Boolean getUser_unread() {
        return user_unread;
    }

    public void setUser_unread(Boolean user_unread) {
        this.user_unread = user_unread;
    }

    public Boolean getAgent_unread() {
        return agent_unread;
    }

    public void setAgent_unread(Boolean agent_unread) {
        this.agent_unread = agent_unread;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getAgent() {
        return agent;
    }

    public void setAgent(User agent) {
        this.agent = agent;
    }

    public EntityLivestock getLivestock() {
        return livestock;
    }

    public void setLivestock(EntityLivestock livestock) {
        this.livestock = livestock;
    }

    public String getAgent_avatar_location() {
        return agent_avatar_location;
    }

    public void setAgent_avatar_location(String agent_avatar_location) {
        this.agent_avatar_location = agent_avatar_location;
    }

    public String getAgent_name() {
        return agent_name;
    }

    public void setAgent_name(String agent_name) {
        this.agent_name = agent_name;
    }

    public String getUser_avatar_location() {
        return user_avatar_location;
    }

    public void setUser_avatar_location(String user_avatar_location) {
        this.user_avatar_location = user_avatar_location;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getLivestockName() {
        return livestockName;
    }

    public void setLivestockName(String livestockName) {
        this.livestockName = livestockName;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeValue(this.user_id);
        dest.writeValue(this.livestock_id);
        dest.writeString(this.room_id);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeValue(this.agent_id);
        dest.writeValue(this.user_unread);
        dest.writeValue(this.agent_unread);
        dest.writeParcelable(this.user, flags);
        dest.writeParcelable(this.agent, flags);
        dest.writeParcelable(this.livestock, flags);
        dest.writeString(this.agent_avatar_location);
        dest.writeString(this.agent_name);
        dest.writeString(this.user_avatar_location);
        dest.writeString(this.user_name);
        dest.writeString(this.livestockName);
    }

    public MyChat() {
    }

    protected MyChat(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.user_id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.livestock_id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.room_id = in.readString(); //(Integer.class.getClassLoader());
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.agent_id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.user_unread = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.agent_unread = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.user = in.readParcelable(User.class.getClassLoader());
        this.agent = in.readParcelable(User.class.getClassLoader());
        this.livestock = in.readParcelable(EntityLivestock.class.getClassLoader());
        this.agent_avatar_location = in.readString();
        this.agent_name = in.readString();
        this.user_avatar_location = in.readString();
        this.user_name = in.readString();
        this.livestockName = in.readString();
    }

    public static final Creator<MyChat> CREATOR = new Creator<MyChat>() {
        @Override
        public MyChat createFromParcel(Parcel source) {
            return new MyChat(source);
        }

        @Override
        public MyChat[] newArray(int size) {
            return new MyChat[size];
        }
    };
}



