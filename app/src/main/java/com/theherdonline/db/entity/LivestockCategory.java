package com.theherdonline.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

@Entity(tableName = "tableCategory")
public class LivestockCategory implements Parcelable {

    @PrimaryKey
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("path")
    @Expose
    private String path;

    @Override
    public String toString() {
        return name;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeString(this.path);
    }

    public LivestockCategory() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LivestockCategory)) return false;
        LivestockCategory that = (LivestockCategory) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, path);
    }

    protected LivestockCategory(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.path = in.readString();
    }

    public LivestockCategory(Integer id) {
        this.id = id;
    }
    public static final Parcelable.Creator<LivestockCategory> CREATOR = new Parcelable.Creator<LivestockCategory>() {
        @Override
        public LivestockCategory createFromParcel(Parcel source) {
            return new LivestockCategory(source);
        }

        @Override
        public LivestockCategory[] newArray(int size) {
            return new LivestockCategory[size];
        }
    };
}
