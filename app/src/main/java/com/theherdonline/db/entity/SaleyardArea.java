
package com.theherdonline.db.entity;

import java.util.List;
import java.util.Objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SaleyardArea {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("saleyard_area_category_id")
    @Expose
    private Integer saleyardAreaCategoryId;
    @SerializedName("saleyard_id")
    @Expose
    private Integer saleyardId;
    @SerializedName("name")
    @Expose
    private String name;
    /*@SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;*/
    @SerializedName("saleyard_assets")
    @Expose
    private List<SaleyardAsset> saleyardAssets = null;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSaleyardAreaCategoryId() {
        return saleyardAreaCategoryId;
    }

    public void setSaleyardAreaCategoryId(Integer saleyardAreaCategoryId) {
        this.saleyardAreaCategoryId = saleyardAreaCategoryId;
    }

    public Integer getSaleyardId() {
        return saleyardId;
    }

    public void setSaleyardId(Integer saleyardId) {
        this.saleyardId = saleyardId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SaleyardAsset> getSaleyardAssets() {
        return saleyardAssets;
    }

    public void setSaleyardAssets(List<SaleyardAsset> saleyardAssets) {
        this.saleyardAssets = saleyardAssets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SaleyardArea)) return false;
        SaleyardArea that = (SaleyardArea) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(saleyardAreaCategoryId, that.saleyardAreaCategoryId) &&
                Objects.equals(saleyardId, that.saleyardId) &&
                Objects.equals(name, that.name) &&
                Objects.equals(saleyardAssets, that.saleyardAssets);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, saleyardAreaCategoryId, saleyardId, name, saleyardAssets);
    }
}
