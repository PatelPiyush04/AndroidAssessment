package com.theherdonline.db.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.android.libraries.places.api.model.Place;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.theherdonline.app.AppConstants;
import com.theherdonline.db.entity.extraattributes.AuctionsPlus;
import com.theherdonline.db.entity.extraattributes.ExtraAttributes;
import com.theherdonline.db.entity.extraattributes.Photo;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(tableName = "EntityLivestock")
public class EntityLivestock extends Object implements Parcelable {

    public static final Creator<EntityLivestock> CREATOR = new Creator<EntityLivestock>() {
        @Override
        public EntityLivestock createFromParcel(Parcel source) {
            return new EntityLivestock(source);
        }

        @Override
        public EntityLivestock[] newArray(int size) {
            return new EntityLivestock[size];
        }
    };
    @PrimaryKey
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("sex")
    @Expose
    private String sex;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("start_date")
    @Expose
    private String start_date;
    @SerializedName("end_date")
    @Expose
    private String end_date;
    @SerializedName("available_at")
    @Expose
    private String available_at;
    @SerializedName("is_pick_up_asap")
    @Expose
    private Integer asap;
    @SerializedName("pick_up_at")
    @Expose
    private String earliest_at;
    @SerializedName("latest_pick_up_at")
    @Expose
    private String latest_at;
    @SerializedName("duration")
    @Expose
    private Integer duration;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("age")
    @Expose
    private Integer age;
    @SerializedName("average_age")
    @Expose
    private Integer average_age;
    @SerializedName("max_age")
    @Expose
    private Integer max_age;
    @SerializedName("lowest_weight")
    @Expose
    private Float lowestWeight;
    @SerializedName("highest_weight")
    @Expose
    private Float highestWeight;
    @SerializedName("average_weight")
    @Expose
    private Float averageWeight;
    @SerializedName("pregnancy_status")
    @Expose
    private String pregnancyStatus;
    @SerializedName("weighed")
    @Expose
    private Integer weighed;
    @SerializedName("dentition")
    @Expose
    private Integer dentition;
    @SerializedName("mouthed")
    @Expose
    private Integer mouthed;
    @SerializedName("eu")
    @Expose
    private Integer eu;
    @SerializedName("msa")
    @Expose
    private Integer msa;
    @SerializedName("lpa")
    @Expose
    private Integer lpa;
    @SerializedName("organic")
    @Expose
    private Integer organic;
    @SerializedName("pcas")
    @Expose
    private Integer pcas;
    @SerializedName("vendor_bred")
    @Expose
    private Integer vendorBred;
    @SerializedName("is_grass_fed")
    @Expose
    private Integer grassFed;
    @SerializedName("is_antibiotic_free")
    @Expose
    private Integer antibioticFree;
    @SerializedName("is_life_time_traceable")
    @Expose
    private Integer lifeTimeTraceable;
    @SerializedName("is_one_mark")
    @Expose
    private Integer oneMark;
    @SerializedName("is_hgp_free")
    @Expose
    private Integer hgpFree;
    @SerializedName("assessed_by")
    @Expose
    private String assessedBy;

    public String getAssessedBy() {
        return assessedBy;
    }

    public void setAssessedBy(String assessedBy) {
        this.assessedBy = assessedBy;
    }

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
    @SerializedName("advertisement_status_id")
    @Expose
    private Integer advertisementStatusId;
    @SerializedName("livestock_category_id")
    @Expose
    private Integer livestockCategoryId;
    @SerializedName("livestock_sub_category_id")
    @Expose
    private Integer livestockSubCategoryId;
    @SerializedName("livestock_classification_id")
    @Expose
    private Integer livestockClassificationId;
    @SerializedName("producer_id")
    @Expose
    private Integer producerId;
    @SerializedName("agent_id")
    @Expose
    private Integer agentId;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("dollar_price")
    @Expose
    private String dollar_price;
    @SerializedName("age_type")
    @Expose
    private String age_type;
    @Ignore
    @SerializedName("bid_id")
    @Expose
    private Integer bid_id;
    @SerializedName("price_type")
    @Expose
    private String price_type;
    @SerializedName("ono")
    @Expose
    private Integer ono;
    @SerializedName("offer_type")
    @Expose
    private String offer_type;
    @SerializedName("price_low")
    @Expose
    private String priceLow;
    @SerializedName("price_high")
    @Expose
    private String priceHigh;
    @SerializedName("under_offer")
    @Expose
    private Integer under_offer;
    @Ignore
    @SerializedName("gender_id")
    @Expose
    private Integer gender_id;
    @Ignore
    @SerializedName("pregnancy_status_id")
    @Expose
    private Integer pregnancy_status_id;
    @Ignore
    @SerializedName("category")
    @Expose
    private LivestockCategory category;
    @Ignore
    @SerializedName("sub_category")
    @Expose
    private LivestockSubCategory sub_category;
    @Ignore
    @SerializedName("producer")
    @Expose
    private User producer;
    @Ignore
    @SerializedName("agent")
    @Expose
    private User agent;
    @Ignore
    @SerializedName("organisation")
    @Expose
    private Organisation organisation;
    @SerializedName("bid_count")
    @Expose
    private Integer bid_count;
    @SerializedName("has_liked")
    @Expose
    private Boolean has_liked;
    private String mediaList;
    @Ignore
    @SerializedName("media")
    @Expose
    private List<Media> media = null;
    @Ignore
    @SerializedName("my_chat")
    @Expose
    private MyChat myChat = null;
    @Ignore
    private Place mPlace = null;
    @Ignore
    @SerializedName("auctions_plus_id")
    @Expose
    private String auctions_plus_id = null;
    @Ignore
    @SerializedName("auctions_plus_img_url")
    @Expose
    private String auctions_plus_img_url = null;
    @Ignore
    @SerializedName("auctions_plus_url")
    @Expose
    private String auctions_plus_url = null;
    @Ignore
    @SerializedName("extra_attributes")
    @Expose
    private Object extra_data;
    @Ignore
    private ExtraAttributes extra_attributes_tmm = null;

    public EntityLivestock() {
    }

    protected EntityLivestock(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.description = in.readString();
        this.sex = in.readString();
        this.price = in.readString();
        this.start_date = in.readString();
        this.end_date = in.readString();
        this.available_at = in.readString();
        this.duration = (Integer) in.readValue(Integer.class.getClassLoader());
        this.quantity = (Integer) in.readValue(Integer.class.getClassLoader());
        this.age = (Integer) in.readValue(Integer.class.getClassLoader());
        this.average_age = (Integer) in.readValue(Integer.class.getClassLoader());
        this.max_age = (Integer) in.readValue(Integer.class.getClassLoader());
        this.lowestWeight = (Float) in.readValue(Float.class.getClassLoader());
        this.highestWeight = (Float) in.readValue(Float.class.getClassLoader());
        this.averageWeight = (Float) in.readValue(Float.class.getClassLoader());
        this.pregnancyStatus = in.readString();
        this.weighed = (Integer) in.readValue(Integer.class.getClassLoader());
        this.dentition = (Integer) in.readValue(Integer.class.getClassLoader());
        this.mouthed = (Integer) in.readValue(Integer.class.getClassLoader());
        this.eu = (Integer) in.readValue(Integer.class.getClassLoader());
        this.msa = (Integer) in.readValue(Integer.class.getClassLoader());
        this.lpa = (Integer) in.readValue(Integer.class.getClassLoader());
        this.organic = (Integer) in.readValue(Integer.class.getClassLoader());
        this.pcas = (Integer) in.readValue(Integer.class.getClassLoader());
        this.addressLineOne = in.readString();
        this.addressLineTwo = in.readString();
        this.addressSuburb = in.readString();
        this.addressCity = in.readString();
        this.addressPostcode = in.readString();
        this.lat = (Double) in.readValue(Double.class.getClassLoader());
        this.lng = (Double) in.readValue(Double.class.getClassLoader());
        this.advertisementStatusId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.livestockCategoryId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.livestockSubCategoryId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.livestockClassificationId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.producerId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.agentId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.address = in.readString();
        this.dollar_price = in.readString();
        this.age_type = in.readString();
        this.bid_id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.price_type = in.readString();
        this.ono = (Integer) in.readValue(Integer.class.getClassLoader());
        this.offer_type = in.readString();
        this.under_offer = (Integer) in.readValue(Integer.class.getClassLoader());
        this.gender_id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.pregnancy_status_id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.category = in.readParcelable(LivestockCategory.class.getClassLoader());
        this.sub_category = in.readParcelable(LivestockSubCategory.class.getClassLoader());
        this.producer = in.readParcelable(User.class.getClassLoader());
        this.agent = in.readParcelable(User.class.getClassLoader());
        this.organisation = in.readParcelable(Organisation.class.getClassLoader());
        this.bid_count = (Integer) in.readValue(Integer.class.getClassLoader());
        this.has_liked = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.mediaList = in.readString();
        this.media = in.createTypedArrayList(Media.CREATOR);
        this.myChat = in.readParcelable(MyChat.class.getClassLoader());
        this.mPlace = in.readParcelable(Place.class.getClassLoader());
        this.auctions_plus_id = in.readString();
        this.auctions_plus_img_url = in.readString();
        this.auctions_plus_url = in.readString();

    }

    public Integer getAsap() {
        return asap;
    }

    public void setAsap(Integer asap) {
        this.asap = asap;
    }

    public String getPriceLow() {
        return priceLow;
    }

    public void setPriceLow(String priceLow) {
        this.priceLow = priceLow;
    }

    public String getPriceHigh() {
        return priceHigh;
    }

    public void setPriceHigh(String priceHigh) {
        this.priceHigh = priceHigh;
    }

    public Integer getVendorBred() {
        return vendorBred;
    }

    public void setVendorBred(Integer vendorBred) {
        this.vendorBred = vendorBred;
    }

    public Integer getGrassFed() {
        return grassFed;
    }

    public void setGrassFed(Integer grassFed) {
        this.grassFed = grassFed;
    }

    public Integer getAntibioticFree() {
        return antibioticFree;
    }

    public void setAntibioticFree(Integer antibioticFree) {
        this.antibioticFree = antibioticFree;
    }

    public Integer getLifeTimeTraceable() {
        return lifeTimeTraceable;
    }

    public void setLifeTimeTraceable(Integer lifeTimeTraceable) {
        this.lifeTimeTraceable = lifeTimeTraceable;
    }

    public Integer getOneMark() {
        return oneMark;
    }

    public void setOneMark(Integer oneMark) {
        this.oneMark = oneMark;
    }

    public Integer getHgpFree() {
        return hgpFree;
    }

    public void setHgpFree(Integer hgpFree) {
        this.hgpFree = hgpFree;
    }

    public String getEarliest_at() {
        return earliest_at;
    }

    public void setEarliest_at(String earliest_at) {
        this.earliest_at = earliest_at;
    }

    public String getLatest_at() {
        return latest_at;
    }

    public void setLatest_at(String latest_at) {
        this.latest_at = latest_at;
    }

    public Object getExtra_data() {
        return extra_data;
    }

    public void setExtra_data(Object extra_data) {
        this.extra_data = extra_data;
    }

    public String getAuctions_plus_id() {
        return auctions_plus_id;
    }

    public void setAuctions_plus_id(String auctions_plus_id) {
        this.auctions_plus_id = auctions_plus_id;
    }

    public String getAuctions_plus_img_url() {
        return auctions_plus_img_url;
    }

    public void setAuctions_plus_img_url(String auctions_plus_img_url) {
        this.auctions_plus_img_url = auctions_plus_img_url;
    }

    public String getAuctions_plus_url() {
        return auctions_plus_url;
    }

    public void setAuctions_plus_url(String auctions_plus_url) {
        this.auctions_plus_url = auctions_plus_url;
    }

    public ExtraAttributes getExtra_attributes() {
        if (extra_attributes_tmm == null) {
            if (extra_data == null || auctions_plus_id == null) {
                return null;
            } else {
                Gson mGson = new Gson();

                try {
                    JsonObject jsonObject = mGson.toJsonTree(extra_data).getAsJsonObject().getAsJsonObject("auctions_plus");  //((LinkedTreeMap)extra_data).;
                    extra_attributes_tmm = new ExtraAttributes();
                    extra_attributes_tmm.setAuctionsPlus(mGson.fromJson(jsonObject, AuctionsPlus.class));
                    return extra_attributes_tmm;
                } catch (JsonSyntaxException e) {
                    return null;
                }
            }
        } else {
            return extra_attributes_tmm;
        }
    }

    public void setExtra_attributes(ExtraAttributes extra_attributes_tmm) {
        this.extra_attributes_tmm = extra_attributes_tmm;
    }

    public void initMediaList() {
        if (media != null) {
            for (Media item : media) {
                mediaList += item.getUrl() + "&";
            }
        }
    }

    public String[] getMediaArray() {
        if (mediaList != null) {
            return mediaList.split("&");
        }
        return null;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUnder_offer() {
        return under_offer;
    }

    public void setUnder_offer(Integer under_offer) {
        this.under_offer = under_offer;
    }

    public Boolean isUnderOffer() {
        return under_offer != null && under_offer != 0;
    }

    public String getName() {
        if (getAuctions_plus_id() == null) {
            return name;
        } else {
            return StringUtils.defaultString(getExtra_attributes().getAuctionsPlus().getTitle());
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        //   if (getAuctions_plus_id() == null) {
        return description;
        // }
        //else
        //{
        // return getExtra_attributes().getAuctionsPlus()
        //}
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        if (getAuctions_plus_id() == null) {
            return end_date;
        } else {
            return null;
        }
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getAvailable_at() {
        if (available_at != null && available_at.length() >= 10) {
            return available_at.substring(0, 10);
        } else {
            return available_at;
        }
    }

    public void setAvailable_at(String available_at) {
        this.available_at = available_at;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getQuantity() {
        if (getAuctions_plus_id() == null) {
            return quantity;
        } else {
            return getExtra_attributes().getAuctionsPlus().getNoOfHead();
        }
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Float getLowestWeight() {
        return lowestWeight;
    }

    public void setLowestWeight(Float lowestWeight) {
        this.lowestWeight = lowestWeight;
    }

    public Float getHighestWeight() {
        return highestWeight;
    }

    public void setHighestWeight(Float highestWeight) {
        this.highestWeight = highestWeight;
    }

    public Float getAverageWeight() {
        return averageWeight;
    }

    public void setAverageWeight(Float averageWeight) {
        this.averageWeight = averageWeight;
    }

    public String getPregnancyStatus() {
        return pregnancyStatus;
    }

    public void setPregnancyStatus(String pregnancyStatus) {
        this.pregnancyStatus = pregnancyStatus;
    }

    public Integer getWeighed() {
        return weighed;
    }

    public void setWeighed(Integer weighed) {
        this.weighed = weighed;
    }

    public Integer getDentition() {
        return dentition;
    }

    public void setDentition(Integer dentition) {
        this.dentition = dentition;
    }

    public Integer getMouthed() {
        return mouthed;
    }

    public void setMouthed(Integer mouthed) {
        this.mouthed = mouthed;
    }

    public Integer getEu() {
        return eu;
    }

    public void setEu(Integer eu) {
        this.eu = eu;
    }

    public Integer getMsa() {
        return msa;
    }

    public void setMsa(Integer msa) {
        this.msa = msa;
    }

    public Integer getLpa() {
        return lpa;
    }

    public void setLpa(Integer lpa) {
        this.lpa = lpa;
    }

    public Integer getOrganic() {
        return organic;
    }

    public void setOrganic(Integer organic) {
        this.organic = organic;
    }

    public Integer getPcas() {
        return pcas;
    }

    public void setPcas(Integer pcas) {
        this.pcas = pcas;
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
        if (getAuctions_plus_id() == null) {
            return lat;
        } else {
            return getExtra_attributes().getAuctionsPlus().getLatitude();
        }
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        if (getAuctions_plus_id() == null) {
            return lng;
        } else {
            return getExtra_attributes().getAuctionsPlus().getLongitude();
        }
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Integer getAdvertisementStatusId() {
        return advertisementStatusId;
    }

    public void setAdvertisementStatusId(Integer advertisementStatusId) {
        this.advertisementStatusId = advertisementStatusId;
    }

    public Integer getLivestockCategoryId() {
        return livestockCategoryId;
    }

    public void setLivestockCategoryId(Integer livestockCategoryId) {
        this.livestockCategoryId = livestockCategoryId;
    }

    public Integer getLivestockSubCategoryId() {
        return livestockSubCategoryId;
    }

    public void setLivestockSubCategoryId(Integer livestockSubCategoryId) {
        this.livestockSubCategoryId = livestockSubCategoryId;
    }

    public Integer getLivestockClassificationId() {
        return livestockClassificationId;
    }

    public void setLivestockClassificationId(Integer livestockClassificationId) {
        this.livestockClassificationId = livestockClassificationId;
    }

    public Integer getProducerId() {
        return producerId;
    }

    public void setProducerId(Integer producerId) {
        this.producerId = producerId;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public String getAddress() {
        if (getAuctions_plus_id() == null) {
            return address;
        } else {
            AuctionsPlus extraAttributes = getExtra_attributes().getAuctionsPlus();
            String add = String.format("%s, %s, %s, %s",
                    StringUtils.defaultString(extraAttributes.getTown()),
                    StringUtils.defaultString(extraAttributes.getRegion()),
                    StringUtils.defaultString(extraAttributes.getState()),
                    StringUtils.defaultString(extraAttributes.getPostCode()));
            return add;
        }
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LivestockCategory getCategory() {
        if (getAuctions_plus_id() == null) {
            return category;
        } else {
            LivestockCategory livestockCategory = new LivestockCategory();
            livestockCategory.setName(StringUtils.defaultString(getExtra_attributes().getAuctionsPlus().getSpecies()));
            livestockCategory.setPath(AppConstants.TAG_auction_plus_url);
            return category;
        }
    }

    public void setCategory(LivestockCategory category) {
        this.category = category;
    }

    public User getProducer() {
        return producer;
    }

    public void setProducer(User producer) {
        this.producer = producer;
    }

    public User getAgent() {
        return agent;
    }

    public void setAgent(User agent) {
        this.agent = agent;
    }

    public String getMediaList() {
        return mediaList;
    }

    public void setMediaList(String mediaList) {
        this.mediaList = mediaList;
    }

    public List<Media> getMedia() {
        if (getAuctions_plus_id() == null) {
            return media;
        } else {
            List<Photo> photoList = getExtra_attributes().getAuctionsPlus().getPhotos();
            // List<Video> videoList = getExtra_attributes().getAuctionsPlus().getVideos();
            List<Media> mediaList = new ArrayList<>();
            if (photoList != null) {
                for (Photo photo : photoList) {
                    Media media = new Media();
                    media.setName(AppConstants.TAG_images);
                    media.setUrl(photo.getPath());
                    mediaList.add(media);
                }

            }
           /* if (videoList != null)
            {
                for (Video video : videoList)
                {
                    Media media = new Media();
                    media.setName(AppConstants.TAG_videos);
                    media.setUrl(video.getPath());
                    mediaList.add(media);
                }
            }*/
            return mediaList.size() == 0 ? null : mediaList;
        }
    }

    public void setMedia(List<Media> media) {
        this.media = media;
    }

    public Integer getBid_count() {
        if (getAuctions_plus_id() == null) {
            return bid_count;
        } else {
            return null;
        }
    }

    public void setBid_count(Integer bid_count) {
        this.bid_count = bid_count;
    }

    public Boolean getHas_liked() {
        return has_liked;
    }

    public void setHas_liked(Boolean has_liked) {
        this.has_liked = has_liked;
    }

    public Integer getBid_id() {
        return bid_id;
    }

    public void setBid_id(Integer bid_id) {
        this.bid_id = bid_id;
    }

    public Integer getGender_id() {
        return gender_id;
    }

    /*  public List<Chat> getChats() {
        return chats;
    }

    public void setChats(List<Chat> chats) {
        this.chats = chats;
    }*/

    public void setGender_id(Integer gender_id) {
        this.gender_id = gender_id;
    }

    public Integer getPregnancy_status_id() {
        return pregnancy_status_id;
    }

    public void setPregnancy_status_id(Integer pregnancy_status_id) {
        this.pregnancy_status_id = pregnancy_status_id;
    }

    public MyChat getMyChat() {
        return myChat;
    }

    public void setMyChat(MyChat myChat) {
        this.myChat = myChat;
    }

    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    public LivestockSubCategory getSub_category() {
        if (getAuctions_plus_id() == null) {
            return sub_category;
        } else {
            LivestockSubCategory livestockSubCategory = new LivestockSubCategory();
            if (getExtra_attributes().getAuctionsPlus().getBreeds() != null && getExtra_attributes().getAuctionsPlus().getBreeds().size() != 0) {
                livestockSubCategory.setName(StringUtils.defaultString(getExtra_attributes().getAuctionsPlus().getBreeds().get(0).getDam()));
            } else {
                livestockSubCategory.setName("");
            }
            return livestockSubCategory;
        }
    }

    public void setSub_category(LivestockSubCategory sub_category) {
        this.sub_category = sub_category;
    }

    public String getDollar_price() {
        return dollar_price;
    }

    public void setDollar_price(String dollar_price) {
        this.dollar_price = dollar_price;
    }

    public String getAge_type() {
        return age_type;
    }

    public void setAge_type(String age_type) {
        this.age_type = age_type;
    }

    public Place getmPlace() {
        return mPlace;
    }

    public void setmPlace(Place mPlace) {
        this.mPlace = mPlace;
    }

    public Integer getAverage_age() {
        return average_age;
    }

    public void setAverage_age(Integer average_age) {
        this.average_age = average_age;
    }

    public Integer getMax_age() {
        return max_age;
    }

    public void setMax_age(Integer max_age) {
        this.max_age = max_age;
    }

    public String getPrice_type() {
        return price_type;
    }

    public void setPrice_type(String price_type) {
        this.price_type = price_type;
    }

    public Integer getOno() {
        return ono;
    }

    public void setOno(Integer ono) {
        this.ono = ono;
    }

    public String getOffer_type() {
        return offer_type;
    }

    public void setOffer_type(String offer_type) {
        this.offer_type = offer_type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EntityLivestock)) return false;
        EntityLivestock that = (EntityLivestock) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(sex, that.sex) &&
                Objects.equals(price, that.price) &&
                Objects.equals(start_date, that.start_date) &&
                Objects.equals(end_date, that.end_date) &&
                Objects.equals(available_at, that.available_at) &&
                Objects.equals(duration, that.duration) &&
                Objects.equals(quantity, that.quantity) &&
                Objects.equals(age, that.age) &&
                Objects.equals(average_age, that.average_age) &&
                Objects.equals(max_age, that.max_age) &&
                Objects.equals(lowestWeight, that.lowestWeight) &&
                Objects.equals(highestWeight, that.highestWeight) &&
                Objects.equals(averageWeight, that.averageWeight) &&
                Objects.equals(pregnancyStatus, that.pregnancyStatus) &&
                Objects.equals(weighed, that.weighed) &&
                Objects.equals(dentition, that.dentition) &&
                Objects.equals(mouthed, that.mouthed) &&
                Objects.equals(eu, that.eu) &&
                Objects.equals(msa, that.msa) &&
                Objects.equals(lpa, that.lpa) &&
                Objects.equals(organic, that.organic) &&
                Objects.equals(vendorBred, that.vendorBred) &&
                Objects.equals(pcas, that.pcas) &&
                Objects.equals(grassFed, that.grassFed) &&
                Objects.equals(antibioticFree, that.antibioticFree) &&
                Objects.equals(lifeTimeTraceable, that.lifeTimeTraceable) &&
                Objects.equals(oneMark, that.oneMark) &&
                Objects.equals(hgpFree, that.hgpFree) &&
                Objects.equals(addressLineOne, that.addressLineOne) &&
                Objects.equals(addressLineTwo, that.addressLineTwo) &&
                Objects.equals(addressSuburb, that.addressSuburb) &&
                Objects.equals(addressCity, that.addressCity) &&
                Objects.equals(addressPostcode, that.addressPostcode) &&
                Objects.equals(lat, that.lat) &&
                Objects.equals(lng, that.lng) &&
                Objects.equals(advertisementStatusId, that.advertisementStatusId) &&
                Objects.equals(livestockCategoryId, that.livestockCategoryId) &&
                Objects.equals(livestockSubCategoryId, that.livestockSubCategoryId) &&
                Objects.equals(livestockClassificationId, that.livestockClassificationId) &&
                Objects.equals(producerId, that.producerId) &&
                Objects.equals(agentId, that.agentId) &&
                Objects.equals(address, that.address) &&
                Objects.equals(dollar_price, that.dollar_price) &&
                Objects.equals(age_type, that.age_type) &&
                Objects.equals(bid_id, that.bid_id) &&
                Objects.equals(price_type, that.price_type) &&
                Objects.equals(ono, that.ono) &&
                Objects.equals(priceLow, that.priceLow) &&
                Objects.equals(priceHigh, that.priceHigh) &&
                Objects.equals(asap, that.asap) &&
                Objects.equals(earliest_at, that.earliest_at) &&
                Objects.equals(latest_at, that.latest_at) &&
                Objects.equals(assessedBy, that.assessedBy) &&
                Objects.equals(offer_type, that.offer_type) &&
                Objects.equals(under_offer, that.under_offer) &&
                Objects.equals(gender_id, that.gender_id) &&
                Objects.equals(pregnancy_status_id, that.pregnancy_status_id) &&
                Objects.equals(category, that.category) &&
                Objects.equals(sub_category, that.sub_category) &&
                Objects.equals(producer, that.producer) &&
                Objects.equals(agent, that.agent) &&
                Objects.equals(organisation, that.organisation) &&
                Objects.equals(bid_count, that.bid_count) &&
                Objects.equals(has_liked, that.has_liked) &&
                Objects.equals(mediaList, that.mediaList) &&
                Objects.equals(media, that.media) &&
                Objects.equals(myChat, that.myChat) &&
                Objects.equals(mPlace, that.mPlace) &&
                Objects.equals(auctions_plus_id, that.auctions_plus_id) &&
                Objects.equals(auctions_plus_img_url, that.auctions_plus_img_url) &&
                Objects.equals(auctions_plus_url, that.auctions_plus_url) &&
                Objects.equals(extra_data, that.extra_data) &&
                Objects.equals(extra_attributes_tmm, that.extra_attributes_tmm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, sex, price,priceLow,priceHigh, start_date, end_date, available_at,
                earliest_at,latest_at, duration, quantity, age, average_age, max_age, lowestWeight, highestWeight, averageWeight, pregnancyStatus, weighed, dentition, mouthed, eu, msa, lpa,
                organic,vendorBred, pcas,grassFed,antibioticFree,lifeTimeTraceable,oneMark,hgpFree, addressLineOne, addressLineTwo, addressSuburb, addressCity, addressPostcode, lat, lng,
                advertisementStatusId, livestockCategoryId, livestockSubCategoryId, livestockClassificationId, producerId, agentId, address, dollar_price, age_type, bid_id, price_type,
                ono,asap, offer_type, under_offer, gender_id, pregnancy_status_id, category, sub_category, producer, agent, organisation, bid_count, has_liked, mediaList, media, myChat, mPlace, auctions_plus_id, auctions_plus_img_url, auctions_plus_url, extra_data, extra_attributes_tmm);
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
        dest.writeString(this.sex);
        dest.writeString(this.price);
        dest.writeString(this.priceLow);
        dest.writeString(this.priceHigh);
        dest.writeString(this.start_date);
        dest.writeString(this.end_date);
        dest.writeString(this.available_at);
        dest.writeString(this.earliest_at);
        dest.writeString(this.latest_at);
        dest.writeValue(this.duration);
        dest.writeValue(this.quantity);
        dest.writeValue(this.age);
        dest.writeValue(this.average_age);
        dest.writeValue(this.max_age);
        dest.writeValue(this.lowestWeight);
        dest.writeValue(this.highestWeight);
        dest.writeValue(this.averageWeight);
        dest.writeString(this.pregnancyStatus);
        dest.writeValue(this.weighed);
        dest.writeValue(this.dentition);
        dest.writeValue(this.mouthed);
        dest.writeValue(this.eu);
        dest.writeValue(this.msa);
        dest.writeValue(this.lpa);
        dest.writeValue(this.organic);
        dest.writeValue(this.vendorBred);
        dest.writeValue(this.pcas);
        dest.writeValue(this.grassFed);
        dest.writeValue(this.antibioticFree);
        dest.writeValue(this.lifeTimeTraceable);
        dest.writeValue(this.oneMark);
        dest.writeValue(this.hgpFree);
        dest.writeString(this.addressLineOne);
        dest.writeString(this.addressLineTwo);
        dest.writeString(this.addressSuburb);
        dest.writeString(this.addressCity);
        dest.writeString(this.addressPostcode);
        dest.writeValue(this.lat);
        dest.writeValue(this.lng);
        dest.writeValue(this.advertisementStatusId);
        dest.writeValue(this.livestockCategoryId);
        dest.writeValue(this.livestockSubCategoryId);
        dest.writeValue(this.livestockClassificationId);
        dest.writeValue(this.producerId);
        dest.writeValue(this.agentId);
        dest.writeString(this.address);
        dest.writeString(this.dollar_price);
        dest.writeString(this.age_type);
        dest.writeValue(this.bid_id);
        dest.writeString(this.price_type);
        dest.writeValue(this.ono);
        dest.writeValue(this.asap);
        dest.writeString(this.offer_type);
        dest.writeValue(this.under_offer);
        dest.writeValue(this.gender_id);
        dest.writeValue(this.pregnancy_status_id);
        dest.writeParcelable(this.category, flags);
        dest.writeParcelable(this.sub_category, flags);
        dest.writeParcelable(this.producer, flags);
        dest.writeParcelable(this.agent, flags);
        dest.writeParcelable(this.organisation, flags);
        dest.writeValue(this.bid_count);
        dest.writeValue(this.has_liked);
        dest.writeString(this.mediaList);
        dest.writeString(this.assessedBy);
        dest.writeTypedList(this.media);
        dest.writeParcelable(this.myChat, flags);
        dest.writeParcelable(this.mPlace, flags);
        dest.writeString(this.auctions_plus_id);
        dest.writeString(this.auctions_plus_img_url);
        dest.writeString(this.auctions_plus_url);

    }
}
