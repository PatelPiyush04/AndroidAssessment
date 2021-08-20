package com.theherdonline.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

@Entity(tableName = "tablePregnancy")
public class PregnancyEntity implements Parcelable {

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
    public String toString() {
        return name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
    }

    public PregnancyEntity() {
    }

    protected PregnancyEntity(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
    }

    public static final Creator<PregnancyEntity> CREATOR = new Creator<PregnancyEntity>() {
        @Override
        public PregnancyEntity createFromParcel(Parcel source) {
            return new PregnancyEntity(source);
        }

        @Override
        public PregnancyEntity[] newArray(int size) {
            return new PregnancyEntity[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PregnancyEntity)) return false;
        PregnancyEntity entity = (PregnancyEntity) o;
        return Objects.equals(id, entity.id) &&
                Objects.equals(name, entity.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
