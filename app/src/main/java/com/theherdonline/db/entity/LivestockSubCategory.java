package com.theherdonline.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

@Entity(tableName = "tableSubCategory")
public class LivestockSubCategory implements Parcelable {

    @PrimaryKey
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("livestock_category_id")
    @Expose
    private Integer livestock_category_id;

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

    public Integer getLivestock_category_id() {
        return livestock_category_id;
    }

    public void setLivestock_category_id(Integer livestock_category_id) {
        this.livestock_category_id = livestock_category_id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeValue(this.livestock_category_id);
    }

    public LivestockSubCategory() {
    }

    protected LivestockSubCategory(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.livestock_category_id = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<LivestockSubCategory> CREATOR = new Parcelable.Creator<LivestockSubCategory>() {
        @Override
        public LivestockSubCategory createFromParcel(Parcel source) {
            return new LivestockSubCategory(source);
        }

        @Override
        public LivestockSubCategory[] newArray(int size) {
            return new LivestockSubCategory[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LivestockSubCategory)) return false;
        LivestockSubCategory that = (LivestockSubCategory) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(livestock_category_id, that.livestock_category_id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, livestock_category_id);
    }
}
