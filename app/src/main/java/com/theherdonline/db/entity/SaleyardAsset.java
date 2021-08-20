
package com.theherdonline.db.entity;

import java.util.List;
import java.util.Objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SaleyardAsset {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("saleyard_area_id")
    @Expose
    private Integer saleyardAreaId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
/*    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;*/
    @SerializedName("weight")
    @Expose
    private Integer weight;
/*    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;*/
    @SerializedName("media")
    @Expose
    private List<Media> media = null;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSaleyardAreaId() {
        return saleyardAreaId;
    }

    public void setSaleyardAreaId(Integer saleyardAreaId) {
        this.saleyardAreaId = saleyardAreaId;
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public List<Media> getMedia() {
        return media;
    }

    public void setMedia(List<Media> media) {
        this.media = media;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SaleyardAsset)) return false;
        SaleyardAsset that = (SaleyardAsset) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(saleyardAreaId, that.saleyardAreaId) &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(quantity, that.quantity) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(weight, that.weight) &&
                Objects.equals(media, that.media);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, saleyardAreaId, name, description, quantity, userId, weight, media);
    }
}
