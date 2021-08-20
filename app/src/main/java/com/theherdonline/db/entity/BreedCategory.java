package com.theherdonline.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "tableBreed")
public class BreedCategory implements Parcelable {

    @PrimaryKey
    private Integer id;

    private String name;

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

    public BreedCategory() {
    }

    protected BreedCategory(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
    }

    public static final Parcelable.Creator<BreedCategory> CREATOR = new Parcelable.Creator<BreedCategory>() {
        @Override
        public BreedCategory createFromParcel(Parcel source) {
            return new BreedCategory(source);
        }

        @Override
        public BreedCategory[] newArray(int size) {
            return new BreedCategory[size];
        }
    };
}
