package com.theherdonline.db.entity;

public class ResCreateLivestock {
    private Status mStatus;
    private Integer mTotal;
    private Integer mFinished;

    public ResCreateLivestock(Status mStatus, Integer mTotal, Integer mFinished) {
        this.mStatus = mStatus;
        this.mTotal = mTotal;
        this.mFinished = mFinished;
    }

    public Status getmStatus() {
        return mStatus;
    }

    public void setmStatus(Status mStatus) {
        this.mStatus = mStatus;
    }

    public Integer getmTotal() {
        return mTotal;
    }

    public void setmTotal(Integer mTotal) {
        this.mTotal = mTotal;
    }

    public Integer getmFinished() {
        return mFinished;
    }

    public void setmFinished(Integer mFinished) {
        this.mFinished = mFinished;
    }

    public enum Status{
        UPDATING_LIVESTOCK,
        UPLOADING_MEDIA,
        FINISH
    }

}
