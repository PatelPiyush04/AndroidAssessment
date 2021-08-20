
package com.theherdonline.db.entity.extraattributes;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuctionsPlus {

    @SerializedName("auctionDetails")
    @Expose
    private AuctionDetails auctionDetails;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("species")
    @Expose
    private String species;
    @SerializedName("stockCategory")
    @Expose
    private String stockCategory;
    @SerializedName("breeds")
    @Expose
    private List<Breed> breeds = null;
    @SerializedName("sex")
    @Expose
    private String sex;
    @SerializedName("saleType")
    @Expose
    private String saleType;
    @SerializedName("noOfHead")
    @Expose
    private Integer noOfHead;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("region")
    @Expose
    private String region;
    @SerializedName("postCode")
    @Expose
    private String postCode;
    @SerializedName("town")
    @Expose
    private String town;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("avgWeight")
    @Expose
    private Double avgWeight;
    @SerializedName("dentition")
    @Expose
    private List<Object> dentition = null;
    @SerializedName("pregnancyInfo")
    @Expose
    private Object pregnancyInfo;
    @SerializedName("age")
    @Expose
    private String age;
    @SerializedName("accreditations")
    @Expose
    private List<Accreditation> accreditations = null;
    @SerializedName("description")
    @Expose
    private Description description;
    @SerializedName("photos")
    @Expose
    private List<Photo> photos = null;
    @SerializedName("videos")
    @Expose
    private List<Video> videos = null;
    @SerializedName("documents")
    @Expose
    private List<Object> documents = null;
    @SerializedName("links")
    @Expose
    private List<Object> links = null;
    @SerializedName("lotStatus")
    @Expose
    private Object lotStatus;
    @SerializedName("perHeadPrice")
    @Expose
    private Object perHeadPrice;
    @SerializedName("lotLink")
    @Expose
    private String lotLink;
    @SerializedName("agentLogoUrl")
    @Expose
    private String agentLogoUrl;
    @SerializedName("agencyTradingName")
    @Expose
    private String agencyTradingName;
    @SerializedName("agentAuctionsPlusId")
    @Expose
    private Integer agentAuctionsPlusId;
    @SerializedName("packageId")
    @Expose
    private Object packageId;

    public AuctionDetails getAuctionDetails() {
        return auctionDetails;
    }

    public void setAuctionDetails(AuctionDetails auctionDetails) {
        this.auctionDetails = auctionDetails;
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

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getStockCategory() {
        return stockCategory;
    }

    public void setStockCategory(String stockCategory) {
        this.stockCategory = stockCategory;
    }

    public List<Breed> getBreeds() {
        return breeds;
    }

    public void setBreeds(List<Breed> breeds) {
        this.breeds = breeds;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSaleType() {
        return saleType;
    }

    public void setSaleType(String saleType) {
        this.saleType = saleType;
    }

    public Integer getNoOfHead() {
        return noOfHead;
    }

    public void setNoOfHead(Integer noOfHead) {
        this.noOfHead = noOfHead;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getAvgWeight() {
        return avgWeight;
    }

    public void setAvgWeight(Double avgWeight) {
        this.avgWeight = avgWeight;
    }

    public List<Object> getDentition() {
        return dentition;
    }

    public void setDentition(List<Object> dentition) {
        this.dentition = dentition;
    }

    public Object getPregnancyInfo() {
        return pregnancyInfo;
    }

    public void setPregnancyInfo(Object pregnancyInfo) {
        this.pregnancyInfo = pregnancyInfo;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public List<Accreditation> getAccreditations() {
        return accreditations;
    }

    public void setAccreditations(List<Accreditation> accreditations) {
        this.accreditations = accreditations;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    public List<Object> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Object> documents) {
        this.documents = documents;
    }

    public List<Object> getLinks() {
        return links;
    }

    public void setLinks(List<Object> links) {
        this.links = links;
    }

    public Object getLotStatus() {
        return lotStatus;
    }

    public void setLotStatus(Object lotStatus) {
        this.lotStatus = lotStatus;
    }

    public Object getPerHeadPrice() {
        return perHeadPrice;
    }

    public void setPerHeadPrice(Object perHeadPrice) {
        this.perHeadPrice = perHeadPrice;
    }

    public String getLotLink() {
        return lotLink;
    }

    public void setLotLink(String lotLink) {
        this.lotLink = lotLink;
    }

    public String getAgentLogoUrl() {
        return agentLogoUrl;
    }

    public void setAgentLogoUrl(String agentLogoUrl) {
        this.agentLogoUrl = agentLogoUrl;
    }

    public String getAgencyTradingName() {
        return agencyTradingName;
    }

    public void setAgencyTradingName(String agencyTradingName) {
        this.agencyTradingName = agencyTradingName;
    }

    public Integer getAgentAuctionsPlusId() {
        return agentAuctionsPlusId;
    }

    public void setAgentAuctionsPlusId(Integer agentAuctionsPlusId) {
        this.agentAuctionsPlusId = agentAuctionsPlusId;
    }

    public Object getPackageId() {
        return packageId;
    }

    public void setPackageId(Object packageId) {
        this.packageId = packageId;
    }

}
