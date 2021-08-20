package com.theherdonline.db.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "tableMarketReport")
public class MarketReport implements Parcelable {

    public static final Creator<MarketReport> CREATOR = new Creator<MarketReport>() {
        @Override
        public MarketReport createFromParcel(Parcel source) {
            return new MarketReport(source);
        }

        @Override
        public MarketReport[] newArray(int size) {
            return new MarketReport[size];
        }
    };
    @PrimaryKey
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("path")
    @Expose
    private String path;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("report_description")
    @Expose
    private String report_description;
    @SerializedName("user_id")
    @Expose
    private Integer user_id;
    @SerializedName("saleyard_id")
    @Expose
    private Integer saleyard_id;
    @SerializedName("sale_at")
    @Expose
    private String sale_at;
    @SerializedName("updated_at")
    @Expose
    private String updated_at;
    @SerializedName("full_path")
    @Expose
    private String full_path;
    @Ignore
    @SerializedName("user")
    @Expose
    private User user;
    @Ignore
    @SerializedName("saleyard")
   // @SerializedName("entitySaleyard")
    @Expose
    private EntitySaleyard entitySaleyard;
    @Ignore
    @SerializedName("media")
    @Expose
    private List<Media> mediaList;


    public MarketReport() {
    }


    protected MarketReport(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.path = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.user_id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.saleyard_id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.sale_at = in.readString();
        this.updated_at = in.readString();
        this.full_path = in.readString();
        this.user = in.readParcelable(User.class.getClassLoader());
        this.entitySaleyard = in.readParcelable(EntitySaleyard.class.getClassLoader());
        this.mediaList = in.createTypedArrayList(Media.CREATOR);
    }

    public String getReport_description() {
        return report_description;
    }

    public void setReport_description(String report_description) {
        this.report_description = report_description;
    }

    public List<Media> getMediaList() {
        return mediaList;
    }

    public void setMediaList(List<Media> mediaList) {
        this.mediaList = mediaList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getSaleyard_id() {
        return saleyard_id;
    }

    public void setSaleyard_id(Integer saleyard_id) {
        this.saleyard_id = saleyard_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public EntitySaleyard getEntitySaleyard() {
        return entitySaleyard;
    }

    public void setEntitySaleyard(EntitySaleyard entitySaleyard) {
        this.entitySaleyard = entitySaleyard;
    }

    public String getSale_at() {
        return sale_at;
    }

    public void setSale_at(String sale_at) {
        this.sale_at = sale_at;
    }

    public String getFull_path() {
        return full_path;
    }

    public void setFull_path(String full_path) {
        this.full_path = full_path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.path);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeValue(this.user_id);
        dest.writeValue(this.saleyard_id);
        dest.writeString(this.sale_at);
        dest.writeString(this.updated_at);
        dest.writeString(this.full_path);
        dest.writeParcelable(this.user, flags);
        dest.writeParcelable(this.entitySaleyard, flags);
        dest.writeTypedList(this.mediaList);
    }
}
