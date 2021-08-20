package com.theherdonline.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;


@Entity(tableName = "tablePaymentCard")
public class PaymentCard {
        @PrimaryKey
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("source_id")
        @Expose
        private String sourceId;
        @SerializedName("exp_month")
        @Expose
        private Integer expMonth;
        @SerializedName("exp_year")
        @Expose
        private Integer expYear;
        @SerializedName("client_secret")
        @Expose
        private String clientSecret;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("brand")
        @Expose
        private String brand;
        @SerializedName("last_four")
        @Expose
        private String lastFour;
        @SerializedName("three_d_secure")
        @Expose
        private String threeDSecure;
        @SerializedName("sourceable_type")
        @Expose
        private String sourceableType;
        @SerializedName("sourceable_id")
        @Expose
        private Integer sourceableId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public Integer getExpMonth() {
        return expMonth;
    }

    public void setExpMonth(Integer expMonth) {
        this.expMonth = expMonth;
    }

    public Integer getExpYear() {
        return expYear;
    }

    public void setExpYear(Integer expYear) {
        this.expYear = expYear;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getLastFour() {
        return lastFour;
    }

    public void setLastFour(String lastFour) {
        this.lastFour = lastFour;
    }

    public String getThreeDSecure() {
        return threeDSecure;
    }

    public void setThreeDSecure(String threeDSecure) {
        this.threeDSecure = threeDSecure;
    }

    public String getSourceableType() {
        return sourceableType;
    }

    public void setSourceableType(String sourceableType) {
        this.sourceableType = sourceableType;
    }

    public Integer getSourceableId() {
        return sourceableId;
    }

    public void setSourceableId(Integer sourceableId) {
        this.sourceableId = sourceableId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaymentCard)) return false;
        PaymentCard that = (PaymentCard) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(sourceId, that.sourceId) &&
                Objects.equals(expMonth, that.expMonth) &&
                Objects.equals(expYear, that.expYear) &&
                Objects.equals(clientSecret, that.clientSecret) &&
                Objects.equals(status, that.status) &&
                Objects.equals(brand, that.brand) &&
                Objects.equals(lastFour, that.lastFour) &&
                Objects.equals(threeDSecure, that.threeDSecure) &&
                Objects.equals(sourceableType, that.sourceableType) &&
                Objects.equals(sourceableId, that.sourceableId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, sourceId, expMonth, expYear, clientSecret, status, brand, lastFour, threeDSecure, sourceableType, sourceableId);
    }
}
