
package com.theherdonline.db.entity;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

@Entity(tableName = "tableUserInfo")
public class User implements Parcelable {

    @PrimaryKey
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("stripe_customer_id")
    @Expose
    private String stripeCustomerId;
    @SerializedName("organisation_id")
    @Expose
    private Integer organisationId;
    @SerializedName("agent_id")
    @Expose
    private Integer agentId;
    @SerializedName("avatar_type")
    @Expose
    private String avatarType;
    @SerializedName("avatar_location")
    @Expose
    private String avatarLocation;
    @SerializedName("phone")
    @Expose
    private String phone;
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
    @SerializedName("job_title")
    @Expose
    private String jobTitle;
    @SerializedName("biography")
    @Expose
    private String biography;
    @SerializedName("facebook_url")
    @Expose
    private String facebookUrl;
    @SerializedName("instagram_url")
    @Expose
    private String instagramUrl;
    @SerializedName("twitter_url")
    @Expose
    private String twitterUrl;
    @SerializedName("youtube_url")
    @Expose
    private String youtubeUrl;
    @SerializedName("active")
    @Expose
    private Boolean active;
    @SerializedName("confirmation_code")
    @Expose
    private String confirmationCode;
    @SerializedName("confirmed")
    @Expose
    private Boolean confirmed;
    @SerializedName("timezone")
    @Expose
    private String timezone;
    @SerializedName("full_name")
    @Expose
    private String fullName;


    @Ignore
    @SerializedName("organisation")
    @Expose
    private Organisation organisation;
    @Ignore
    @SerializedName("roles")
    @Expose
    private List<Role> roles = null;

    @Ignore
    @SerializedName("producers")
    @Expose
    private List<User> producer = null;


    @Ignore
    @SerializedName("permissions")
    @Expose
    private List<Permission> permissions = null;




    @SerializedName("follower_count")
    @Expose
    private Integer followerCount = null;
    @SerializedName("is_authed_user_following")
    @Expose
    private Boolean is_authed_user_following;

    @SerializedName("avatar_location_full_url")
    @Expose
    private String avatar_location_full_url;


    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStripeCustomerId() {
        return stripeCustomerId;
    }

    public void setStripeCustomerId(String stripeCustomerId) {
        this.stripeCustomerId = stripeCustomerId;
    }

    public Integer getOrganisationId() {
        return organisationId;
    }

    public void setOrganisationId(Integer organisationId) {
        this.organisationId = organisationId;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public String getAvatarType() {
        return avatarType;
    }

    public void setAvatarType(String avatarType) {
        this.avatarType = avatarType;
    }

    public String getAvatarLocation() {
        return avatarLocation;
    }

    public void setAvatarLocation(String avatarLocation) {
        this.avatarLocation = avatarLocation;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public String getInstagramUrl() {
        return instagramUrl;
    }

    public void setInstagramUrl(String instagramUrl) {
        this.instagramUrl = instagramUrl;
    }

    public String getTwitterUrl() {
        return twitterUrl;
    }

    public void setTwitterUrl(String twitterUrl) {
        this.twitterUrl = twitterUrl;
    }

    public String getYoutubeUrl() {
        return youtubeUrl;
    }

    public void setYoutubeUrl(String youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getConfirmationCode() {
        return confirmationCode;
    }

    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

    public Boolean getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<User> getProducer() {
        return producer;
    }

    public void setProducer(List<User> producer) {
        this.producer = producer;
    }

    public Integer getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(Integer followerCount) {
        this.followerCount = followerCount;
    }

    public Boolean getIs_authed_user_following() {
        return is_authed_user_following;
    }

    public void setIs_authed_user_following(Boolean is_authed_user_following) {
        this.is_authed_user_following = is_authed_user_following;
    }

    public String getAvatar_location_full_url() {
        return avatar_location_full_url;
    }

    public void setAvatar_location_full_url(String avatar_location_full_url) {
        this.avatar_location_full_url = avatar_location_full_url;
    }


    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(email, user.email) &&
                Objects.equals(stripeCustomerId, user.stripeCustomerId) &&
                Objects.equals(organisationId, user.organisationId) &&
                Objects.equals(agentId, user.agentId) &&
                Objects.equals(avatarType, user.avatarType) &&
                Objects.equals(avatarLocation, user.avatarLocation) &&
                Objects.equals(phone, user.phone) &&
                Objects.equals(addressLineOne, user.addressLineOne) &&
                Objects.equals(addressLineTwo, user.addressLineTwo) &&
                Objects.equals(addressSuburb, user.addressSuburb) &&
                Objects.equals(addressCity, user.addressCity) &&
                Objects.equals(addressPostcode, user.addressPostcode) &&
                Objects.equals(lat, user.lat) &&
                Objects.equals(lng, user.lng) &&
                Objects.equals(jobTitle, user.jobTitle) &&
                Objects.equals(biography, user.biography) &&
                Objects.equals(facebookUrl, user.facebookUrl) &&
                Objects.equals(instagramUrl, user.instagramUrl) &&
                Objects.equals(twitterUrl, user.twitterUrl) &&
                Objects.equals(youtubeUrl, user.youtubeUrl) &&
                Objects.equals(active, user.active) &&
                Objects.equals(confirmationCode, user.confirmationCode) &&
                Objects.equals(confirmed, user.confirmed) &&
                Objects.equals(timezone, user.timezone) &&
                Objects.equals(fullName, user.fullName) &&
                Objects.equals(organisation, user.organisation) &&
                Objects.equals(roles, user.roles) &&
                Objects.equals(producer, user.producer) &&
                Objects.equals(permissions, user.permissions) &&
                Objects.equals(followerCount, user.followerCount) &&
                Objects.equals(is_authed_user_following, user.is_authed_user_following) &&
                Objects.equals(avatar_location_full_url, user.avatar_location_full_url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, stripeCustomerId, organisationId, agentId, avatarType, avatarLocation, phone, addressLineOne, addressLineTwo, addressSuburb, addressCity, addressPostcode, lat, lng, jobTitle, biography, facebookUrl, instagramUrl, twitterUrl, youtubeUrl, active, confirmationCode, confirmed, timezone, fullName, organisation, roles, producer, permissions, followerCount, is_authed_user_following, avatar_location_full_url);
    }

    public User() {
    }

    @Override
    public String toString() {
        return fullName;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.email);
        dest.writeString(this.stripeCustomerId);
        dest.writeValue(this.organisationId);
        dest.writeValue(this.agentId);
        dest.writeString(this.avatarType);
        dest.writeString(this.avatarLocation);
        dest.writeString(this.phone);
        dest.writeString(this.addressLineOne);
        dest.writeString(this.addressLineTwo);
        dest.writeString(this.addressSuburb);
        dest.writeString(this.addressCity);
        dest.writeString(this.addressPostcode);
        dest.writeValue(this.lat);
        dest.writeValue(this.lng);
        dest.writeString(this.jobTitle);
        dest.writeString(this.biography);
        dest.writeString(this.facebookUrl);
        dest.writeString(this.instagramUrl);
        dest.writeString(this.twitterUrl);
        dest.writeString(this.youtubeUrl);
        dest.writeValue(this.active);
        dest.writeString(this.confirmationCode);
        dest.writeValue(this.confirmed);
        dest.writeString(this.timezone);
        dest.writeString(this.fullName);
        dest.writeParcelable(this.organisation, flags);
        dest.writeTypedList(this.roles);
        dest.writeTypedList(this.producer);
        dest.writeTypedList(this.permissions);
        dest.writeValue(this.followerCount);
        dest.writeValue(this.is_authed_user_following);
        dest.writeString(this.avatar_location_full_url);
    }

    protected User(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.email = in.readString();
        this.stripeCustomerId = in.readString();
        this.organisationId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.agentId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.avatarType = in.readString();
        this.avatarLocation = in.readString();
        this.phone = in.readString();
        this.addressLineOne = in.readString();
        this.addressLineTwo = in.readString();
        this.addressSuburb = in.readString();
        this.addressCity = in.readString();
        this.addressPostcode = in.readString();
        this.lat = (Double) in.readValue(Double.class.getClassLoader());
        this.lng = (Double) in.readValue(Double.class.getClassLoader());
        this.jobTitle = in.readString();
        this.biography = in.readString();
        this.facebookUrl = in.readString();
        this.instagramUrl = in.readString();
        this.twitterUrl = in.readString();
        this.youtubeUrl = in.readString();
        this.active = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.confirmationCode = in.readString();
        this.confirmed = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.timezone = in.readString();
        this.fullName = in.readString();
        this.organisation = in.readParcelable(Organisation.class.getClassLoader());
        this.roles = in.createTypedArrayList(Role.CREATOR);
        this.producer = in.createTypedArrayList(User.CREATOR);
        this.permissions = in.createTypedArrayList(Permission.CREATOR);
        this.followerCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.is_authed_user_following = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.avatar_location_full_url = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
