
package com.theherdonline.db.entity;

import androidx.room.ColumnInfo;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Role implements Parcelable {

    @ColumnInfo(name = "role_name")
    @SerializedName("name")
    @Expose
    private String name;

/*    @ColumnInfo(name = "role_guard_name")
    @SerializedName("guard_name")
    @Expose
    private String guardName;*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;
        Role role = (Role) o;
        return Objects.equals(name, role.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name);
    }





    /*   public String getGuardName() {
        return guardName;
    }

    public void setGuardName(String guardName) {
        this.guardName = guardName;
    }*/


/*    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;
        Role role = (Role) o;
        return Objects.equals(name, role.name) &&
                Objects.equals(guardName, role.guardName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, guardName);
    }*/

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
    }

    public Role() {
    }

    protected Role(Parcel in) {
        this.name = in.readString();
    }

    public static final Parcelable.Creator<Role> CREATOR = new Parcelable.Creator<Role>() {
        @Override
        public Role createFromParcel(Parcel source) {
            return new Role(source);
        }

        @Override
        public Role[] newArray(int size) {
            return new Role[size];
        }
    };
}
