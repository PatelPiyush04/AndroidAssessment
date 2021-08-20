
package com.theherdonline.db.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Ignore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.google.android.libraries.places.api.model.Place;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EntitySaleyard extends Object implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("starts_at")
    @Expose
    private String startsAt;
    @SerializedName("published")
    @Expose
    private Integer published;
    @SerializedName("saleyard_status_id")
    @Expose
    private Integer saleyardStatusId;
    @SerializedName("saleyard_category_id")
    @Expose
    private Integer saleyardCategoryId;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("address_line_one")
    @Expose
    private String addressLineOne;
    @SerializedName("address_line_two")
    @Expose
    private String addressLineTwo;
    @SerializedName("address_suburb")
    @Expose
    private String addressSuburb;
    @SerializedName("address_city")
    @Expose
    private String addressCity;
    @SerializedName("address_postcode")
    @Expose
    private String addressPostcode;
    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("lng")
    @Expose
    private Double lng;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("headcount")
    @Expose
    private Integer headcount;

    @SerializedName("user")
    @Expose
    private User user;



    @SerializedName("users")
    @Expose
    private List<User> users = null;
    @SerializedName("saleyard_category")
    @Expose
    private SaleyardCategory saleyardCategory;
    @SerializedName("saleyard_status")
    @Expose
    private SaleyardStatus saleyardStatus;
    @SerializedName("saleyard_areas")
    @Expose
    private List<SaleyardArea> saleyardAreas = null;

    @SerializedName("has_liked")
    @Expose
    private Boolean has_liked;


    @SerializedName("quantity")
    @Expose
    private Integer quantity;

    public String getReport_description() {
        return report_description;
    }

    public void setReport_description(String report_description) {
        this.report_description = report_description;
    }

    @SerializedName("report_description")
    @Expose
    private String report_description;



    @SerializedName("sale_numbers")
    @Expose
    private String sale_numbers;


    @Ignore
    @SerializedName("media")
    @Expose
    private List<Media> media = null;


    public List<Media> getMedia() {
        return media;
    }

    public void setMedia(List<Media> media) {
        this.media = media;
    }

    @Ignore
    private Place mPlace = null;

    public Place getmPlace() {
        return mPlace;
    }

    public void setmPlace(Place mPlace) {
        this.mPlace = mPlace;
    }


    public String getSale_numbers() {
        return sale_numbers;
    }

    public void setSale_numbers(String sale_numbers) {
        this.sale_numbers = sale_numbers;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartsAt() {
        return startsAt;
    }

    public void setStartsAt(String startsAt) {
        this.startsAt = startsAt;
    }

    public Integer getPublished() {
        return published;
    }

    public void setPublished(Integer published) {
        this.published = published;
    }

    public Integer getSaleyardStatusId() {
        return saleyardStatusId;
    }

    public void setSaleyardStatusId(Integer saleyardStatusId) {
        this.saleyardStatusId = saleyardStatusId;
    }

    public Integer getSaleyardCategoryId() {
        return saleyardCategoryId;
    }

    public void setSaleyardCategoryId(Integer saleyardCategoryId) {
        this.saleyardCategoryId = saleyardCategoryId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAddressLineOne() {
        return addressLineOne;
    }

    public void setAddressLineOne(String addressLineOne) {
        this.addressLineOne = addressLineOne;
    }

    public String getAddressLineTwo() {
        return addressLineTwo;
    }

    public void setAddressLineTwo(String addressLineTwo) {
        this.addressLineTwo = addressLineTwo;
    }

    public String getAddressSuburb() {
        return addressSuburb;
    }

    public void setAddressSuburb(String addressSuburb) {
        this.addressSuburb = addressSuburb;
    }

    public String getAddressCity() {
        return addressCity;
    }

    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    public String getAddressPostcode() {
        return addressPostcode;
    }

    public void setAddressPostcode(String addressPostcode) {
        this.addressPostcode = addressPostcode;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<User> getUsers() {
        return users;

/*
        List<User> list = new ArrayList<>();
        for (int i = 0; i < 10 ; i++)
        {
            list.add(users.get(0));
        }
        return list;
*/

    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public SaleyardCategory getSaleyardCategory() {
        return saleyardCategory;
    }

    public void setSaleyardCategory(SaleyardCategory saleyardCategory) {
        this.saleyardCategory = saleyardCategory;
    }

    public SaleyardStatus getSaleyardStatus() {
        return saleyardStatus;
    }

    public void setSaleyardStatus(SaleyardStatus saleyardStatus) {
        this.saleyardStatus = saleyardStatus;
    }

    public List<SaleyardArea> getSaleyardAreas() {
        return saleyardAreas;
    }

    public void setSaleyardAreas(List<SaleyardArea> saleyardAreas) {
        this.saleyardAreas = saleyardAreas;
    }

    public Boolean getHas_liked() {
        return has_liked;
    }

    public void setHas_liked(Boolean has_liked) {
        this.has_liked = has_liked;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getHeadcount() {
        return headcount;
    }

    public void setHeadcount(Integer headcount) {
        this.headcount = headcount;
    }




    public EntitySaleyard() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EntitySaleyard)) return false;
        EntitySaleyard entitySaleyard = (EntitySaleyard) o;
        return Objects.equals(id, entitySaleyard.id) &&
                Objects.equals(name, entitySaleyard.name) &&
                Objects.equals(description, entitySaleyard.description) &&
                Objects.equals(startsAt, entitySaleyard.startsAt) &&
                Objects.equals(published, entitySaleyard.published) &&
                Objects.equals(saleyardStatusId, entitySaleyard.saleyardStatusId) &&
                Objects.equals(saleyardCategoryId, entitySaleyard.saleyardCategoryId) &&
                Objects.equals(userId, entitySaleyard.userId) &&
                Objects.equals(addressLineOne, entitySaleyard.addressLineOne) &&
                Objects.equals(addressLineTwo, entitySaleyard.addressLineTwo) &&
                Objects.equals(addressSuburb, entitySaleyard.addressSuburb) &&
                Objects.equals(addressCity, entitySaleyard.addressCity) &&
                Objects.equals(addressPostcode, entitySaleyard.addressPostcode) &&
                Objects.equals(lat, entitySaleyard.lat) &&
                Objects.equals(lng, entitySaleyard.lng) &&
                Objects.equals(createdAt, entitySaleyard.createdAt) &&
                Objects.equals(updatedAt, entitySaleyard.updatedAt) &&
                Objects.equals(type, entitySaleyard.type) &&
                Objects.equals(address, entitySaleyard.address) &&
                Objects.equals(headcount, entitySaleyard.headcount) &&
                Objects.equals(user, entitySaleyard.user) &&
                Objects.equals(users, entitySaleyard.users) &&
                Objects.equals(saleyardCategory, entitySaleyard.saleyardCategory) &&
                Objects.equals(saleyardStatus, entitySaleyard.saleyardStatus) &&
                Objects.equals(saleyardAreas, entitySaleyard.saleyardAreas) &&
                Objects.equals(has_liked, entitySaleyard.has_liked) &&
                Objects.equals(quantity, entitySaleyard.quantity) &&
                Objects.equals(sale_numbers, entitySaleyard.sale_numbers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, startsAt, published, saleyardStatusId, saleyardCategoryId, userId, addressLineOne, addressLineTwo, addressSuburb, addressCity, addressPostcode, lat, lng, createdAt, updatedAt, type, address, headcount, user, users, saleyardCategory, saleyardStatus, saleyardAreas, has_liked, quantity, sale_numbers);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeString(this.startsAt);
        dest.writeValue(this.published);
        dest.writeValue(this.saleyardStatusId);
        dest.writeValue(this.saleyardCategoryId);
        dest.writeValue(this.userId);
        dest.writeString(this.addressLineOne);
        dest.writeString(this.addressLineTwo);
        dest.writeString(this.addressSuburb);
        dest.writeString(this.addressCity);
        dest.writeString(this.addressPostcode);
        dest.writeValue(this.lat);
        dest.writeValue(this.lng);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
        dest.writeString(this.type);
        dest.writeString(this.address);
        dest.writeValue(this.headcount);
        dest.writeParcelable(this.user, flags);
        dest.writeTypedList(this.users);
        dest.writeParcelable(this.saleyardCategory, flags);
        dest.writeParcelable(this.saleyardStatus, flags);
        dest.writeList(this.saleyardAreas);
        dest.writeValue(this.has_liked);
        dest.writeValue(this.quantity);
        dest.writeString(this.sale_numbers);

    }

    protected EntitySaleyard(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.description = in.readString();
        this.startsAt = in.readString();
        this.published = (Integer) in.readValue(Integer.class.getClassLoader());
        this.saleyardStatusId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.saleyardCategoryId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.userId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.addressLineOne = in.readString();
        this.addressLineTwo = in.readString();
        this.addressSuburb = in.readString();
        this.addressCity = in.readString();
        this.addressPostcode = in.readString();
        this.lat = (Double) in.readValue(Double.class.getClassLoader());
        this.lng = (Double) in.readValue(Double.class.getClassLoader());
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
        this.type = in.readString();
        this.address = in.readString();
        this.headcount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.user = in.readParcelable(User.class.getClassLoader());
        this.users = in.createTypedArrayList(User.CREATOR);
        this.saleyardCategory = in.readParcelable(SaleyardCategory.class.getClassLoader());
        this.saleyardStatus = in.readParcelable(SaleyardStatus.class.getClassLoader());
        this.saleyardAreas = new ArrayList<SaleyardArea>();
        in.readList(this.saleyardAreas, SaleyardArea.class.getClassLoader());
        this.has_liked = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.quantity = (Integer) in.readValue(Integer.class.getClassLoader());
        this.sale_numbers = in.readString();

    }

    public static final Creator<EntitySaleyard> CREATOR = new Creator<EntitySaleyard>() {
        @Override
        public EntitySaleyard createFromParcel(Parcel source) {
            return new EntitySaleyard(source);
        }

        @Override
        public EntitySaleyard[] newArray(int size) {
            return new EntitySaleyard[size];
        }
    };
}
