package com.theherdonline.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "tableAnimalCategory")
public class AnimalCategory implements Parcelable {

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

    public AnimalCategory() {
    }

    protected AnimalCategory(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
    }

    public static final Parcelable.Creator<AnimalCategory> CREATOR = new Parcelable.Creator<AnimalCategory>() {
        @Override
        public AnimalCategory createFromParcel(Parcel source) {
            return new AnimalCategory(source);
        }

        @Override
        public AnimalCategory[] newArray(int size) {
            return new AnimalCategory[size];
        }
    };
}
