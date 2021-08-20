package com.theherdonline.ui.general;

import android.os.Parcel;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.theherdonline.db.entity.GenderEntity;
import com.theherdonline.db.entity.LivestockCategory;
import com.theherdonline.db.entity.LivestockSubCategory;
import com.theherdonline.db.entity.PregnancyEntity;
import com.theherdonline.util.ActivityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class SearchFilterPublicLivestock extends SearchFilter implements Cloneable {

    public static final Creator<SearchFilterPublicLivestock> CREATOR = new Creator<SearchFilterPublicLivestock>() {
        @Override
        public SearchFilterPublicLivestock createFromParcel(Parcel source) {
            return new SearchFilterPublicLivestock(source);
        }

        @Override
        public SearchFilterPublicLivestock[] newArray(int size) {
            return new SearchFilterPublicLivestock[size];
        }
    };
    public String where_like;
    public LatLng latLng;
    public Place mPlace;
    public Float radius;
    public Integer minPrice;
    public Integer maxPrice;
    public Integer minWeight;
    public Integer maxWeight;
    public Integer minAge;
    public Integer maxAge;
    public Integer quantity;
    public Boolean weighted;
    public Boolean dentition;
    public Boolean mounted;
    public Boolean eu;
    public Boolean msa;
    public Boolean lpa;
    public Boolean organic;
    public Boolean pcas;
    public List<ADType> adType;
    public LivestockCategory livestockCategory;
    public LivestockSubCategory livestockSubCategory;
    public Integer producer_id;
    public Integer agent_id;
    public Integer organisation_id;
    public Integer user_id;
    public GenderEntity genderEntity;
    public PregnancyEntity pregnancyEntity;
    public String ageUnit;


    public SearchFilterPublicLivestock() {
    }

    protected SearchFilterPublicLivestock(Parcel in) {
        super(in);
        this.where_like = in.readString();
        this.latLng = in.readParcelable(LatLng.class.getClassLoader());
        this.radius = (Float) in.readValue(Float.class.getClassLoader());
        this.minPrice = (Integer) in.readValue(Integer.class.getClassLoader());
        this.maxPrice = (Integer) in.readValue(Integer.class.getClassLoader());
        this.minWeight = (Integer) in.readValue(Integer.class.getClassLoader());
        this.maxWeight = (Integer) in.readValue(Integer.class.getClassLoader());
        this.minAge = (Integer) in.readValue(Integer.class.getClassLoader());
        this.maxAge = (Integer) in.readValue(Integer.class.getClassLoader());
        this.quantity = (Integer) in.readValue(Integer.class.getClassLoader());
        this.weighted = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.dentition = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.mounted = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.eu = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.msa = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.lpa = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.organic = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.pcas = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.adType = new ArrayList<ADType>();
        in.readList(this.adType, ADType.class.getClassLoader());
        this.livestockCategory = in.readParcelable(LivestockCategory.class.getClassLoader());
        this.livestockSubCategory = in.readParcelable(LivestockSubCategory.class.getClassLoader());
        this.producer_id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.agent_id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.organisation_id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.user_id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.genderEntity = in.readParcelable(LivestockCategory.class.getClassLoader());
        this.pregnancyEntity = in.readParcelable(LivestockSubCategory.class.getClassLoader());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SearchFilterPublicLivestock)) return false;
        if (!super.equals(o)) return false;
        SearchFilterPublicLivestock that = (SearchFilterPublicLivestock) o;


        return Objects.equals(where_like, that.where_like) &&
                Objects.equals(latLng, that.latLng) &&
                ActivityUtils.EqualPlace(mPlace, that.mPlace) &&
                Objects.equals(radius, that.radius) &&
                Objects.equals(minPrice, that.minPrice) &&
                Objects.equals(maxPrice, that.maxPrice) &&
                Objects.equals(minWeight, that.minWeight) &&
                Objects.equals(maxWeight, that.maxWeight) &&
                Objects.equals(minAge, that.minAge) &&
                Objects.equals(maxAge, that.maxAge) &&
                Objects.equals(quantity, that.quantity) &&
                Objects.equals(weighted, that.weighted) &&
                Objects.equals(dentition, that.dentition) &&
                Objects.equals(mounted, that.mounted) &&
                Objects.equals(eu, that.eu) &&
                Objects.equals(msa, that.msa) &&
                Objects.equals(lpa, that.lpa) &&
                Objects.equals(organic, that.organic) &&
                Objects.equals(pcas, that.pcas) &&
                Objects.equals(adType, that.adType) &&
                Objects.equals(livestockCategory, that.livestockCategory) &&
                Objects.equals(livestockSubCategory, that.livestockSubCategory) &&
                Objects.equals(producer_id, that.producer_id) &&
                Objects.equals(agent_id, that.agent_id) &&
                Objects.equals(organisation_id, that.organisation_id) &&
                Objects.equals(user_id, that.user_id) &&
                Objects.equals(genderEntity, that.genderEntity) &&
                Objects.equals(pregnancyEntity, that.pregnancyEntity) &&
                Objects.equals(ageUnit, that.ageUnit);
    }

    public boolean equalsFromPaddock(Object o) {
        if (this == o) return true;
        if (!(o instanceof SearchFilterPublicLivestock)) return false;
        if (!super.equals(o)) return false;
        SearchFilterPublicLivestock that = (SearchFilterPublicLivestock) o;


        return Objects.equals(where_like, that.where_like) &&
                Objects.equals(latLng, that.latLng) &&
                ActivityUtils.EqualPlace(mPlace, that.mPlace) &&
                Objects.equals(radius, that.radius) &&
                Objects.equals(minPrice, that.minPrice) &&
                Objects.equals(maxPrice, that.maxPrice) &&
                Objects.equals(minWeight, that.minWeight) &&
                Objects.equals(maxWeight, that.maxWeight) &&
                Objects.equals(minAge, that.minAge) &&
                Objects.equals(maxAge, that.maxAge) &&
                Objects.equals(quantity, that.quantity) &&
                Objects.equals(weighted, that.weighted) &&
                Objects.equals(dentition, that.dentition) &&
                Objects.equals(mounted, that.mounted) &&
                Objects.equals(eu, that.eu) &&
                Objects.equals(msa, that.msa) &&
                Objects.equals(lpa, that.lpa) &&
                Objects.equals(organic, that.organic) &&
                Objects.equals(pcas, that.pcas) &&
                Objects.equals(adType, that.adType) &&
                Objects.equals(livestockSubCategory, that.livestockSubCategory) &&
                Objects.equals(producer_id, that.producer_id) &&
                Objects.equals(agent_id, that.agent_id) &&
                Objects.equals(organisation_id, that.organisation_id) &&
                Objects.equals(user_id, that.user_id) &&
                Objects.equals(genderEntity, that.genderEntity) &&
                Objects.equals(pregnancyEntity, that.pregnancyEntity) &&
                Objects.equals(ageUnit, that.ageUnit);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), where_like, latLng, mPlace, radius, minPrice, maxPrice, minWeight, maxWeight, minAge, maxAge, quantity, weighted, dentition, mounted, eu, msa, lpa, organic, pcas, adType, livestockCategory, livestockSubCategory, producer_id, agent_id, organisation_id, user_id, genderEntity, pregnancyEntity, ageUnit);
    }

    public Place getmPlace() {
        return mPlace;
    }

    public void setmPlace(Place mPlace) {
        this.mPlace = mPlace;
    }

    @Override
    public String getWhere_like() {
        return where_like;
    }

    @Override
    public void setWhere_like(String where_like) {
        this.where_like = where_like;
    }

    @Override
    public LatLng getLatLng() {
        return latLng;
    }

    @Override
    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    @Override
    public Float getRadius() {
        return radius;
    }

    @Override
    public void setRadius(Float radius) {
        this.radius = radius;
    }

    public Integer getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Integer minPrice) {
        this.minPrice = minPrice;
    }

    public Integer getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Integer maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Integer getMinWeight() {
        return minWeight;
    }

    public void setMinWeight(Integer minWeight) {
        this.minWeight = minWeight;
    }

    public Integer getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(Integer maxWeight) {
        this.maxWeight = maxWeight;
    }

    public Integer getMinAge() {
        return minAge;
    }

    public void setMinAge(Integer minAge) {
        this.minAge = minAge;
    }

    public Integer getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(Integer maxAge) {
        this.maxAge = maxAge;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Boolean getWeighted() {
        return weighted;
    }

    public void setWeighted(Boolean weighted) {
        this.weighted = weighted;
    }

    public Boolean getDentition() {
        return dentition;
    }

    public void setDentition(Boolean dentition) {
        this.dentition = dentition;
    }

    public Boolean getMounted() {
        return mounted;
    }

    public void setMounted(Boolean mounted) {
        this.mounted = mounted;
    }

    public Boolean getEu() {
        return eu;
    }

    public void setEu(Boolean eu) {
        this.eu = eu;
    }

    public Boolean getMsa() {
        return msa;
    }

    public void setMsa(Boolean msa) {
        this.msa = msa;
    }

    public Boolean getLpa() {
        return lpa;
    }

    public void setLpa(Boolean lpa) {
        this.lpa = lpa;
    }

    public Boolean getOrganic() {
        return organic;
    }

    public void setOrganic(Boolean organic) {
        this.organic = organic;
    }

    public Boolean getPcas() {
        return pcas;
    }

    public void setPcas(Boolean pcas) {
        this.pcas = pcas;
    }

    public List<ADType> getAdType() {
        return adType;
    }

    public void setAdType(List<ADType> adType) {
        this.adType = adType;
    }

    public LivestockCategory getLivestockCategory() {
        return livestockCategory;
    }

    public void setLivestockCategory(LivestockCategory livestockCategory) {
        this.livestockCategory = livestockCategory;
    }

    public LivestockSubCategory getLivestockSubCategory() {
        return livestockSubCategory;
    }

    public void setLivestockSubCategory(LivestockSubCategory livestockSubCategory) {
        this.livestockSubCategory = livestockSubCategory;
    }

    public GenderEntity getGenderEntity() {
        return genderEntity;
    }

    public void setGenderEntity(GenderEntity genderEntity) {
        this.genderEntity = genderEntity;
    }

    public PregnancyEntity getPregnancyEntity() {
        return pregnancyEntity;
    }

    public void setPregnancyEntity(PregnancyEntity pregnancyEntity) {
        this.pregnancyEntity = pregnancyEntity;
    }

    public String getAgeUnit() {
        return ageUnit;
    }

    public void setAgeUnit(String ageUnit) {
        this.ageUnit = ageUnit;
    }

    @Override
    public Integer getProducer_id() {
        return producer_id;
    }

    @Override
    public void setProducer_id(Integer producer_id) {
        this.producer_id = producer_id;
    }

    @Override
    public Integer getAgent_id() {
        return agent_id;
    }

    @Override
    public void setAgent_id(Integer agent_id) {
        this.agent_id = agent_id;
    }

    @Override
    public Integer getOrganisation_id() {
        return organisation_id;
    }

    @Override
    public void setOrganisation_id(Integer organisation_id) {
        this.organisation_id = organisation_id;
    }

    @Override
    public Integer getUser_id() {
        return user_id;
    }

    @Override
    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.where_like);
        dest.writeParcelable(this.latLng, flags);
        dest.writeValue(this.radius);
        dest.writeValue(this.minPrice);
        dest.writeValue(this.maxPrice);
        dest.writeValue(this.minWeight);
        dest.writeValue(this.maxWeight);
        dest.writeValue(this.minAge);
        dest.writeValue(this.maxAge);
        dest.writeValue(this.quantity);
        dest.writeValue(this.weighted);
        dest.writeValue(this.dentition);
        dest.writeValue(this.mounted);
        dest.writeValue(this.eu);
        dest.writeValue(this.msa);
        dest.writeValue(this.lpa);
        dest.writeValue(this.organic);
        dest.writeValue(this.pcas);
        dest.writeList(this.adType);
        dest.writeParcelable(this.livestockCategory, flags);
        dest.writeParcelable(this.livestockSubCategory, flags);
        dest.writeValue(this.producer_id);
        dest.writeValue(this.agent_id);
        dest.writeValue(this.organisation_id);
        dest.writeValue(this.user_id);
        dest.writeParcelable(this.genderEntity, flags);
        dest.writeParcelable(this.pregnancyEntity, flags);
    }

    public SearchFilterPublicLivestock clone() throws CloneNotSupportedException {
        return (SearchFilterPublicLivestock) super.clone();
    }
}
