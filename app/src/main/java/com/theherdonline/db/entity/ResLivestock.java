package com.theherdonline.db.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResLivestock extends PagerData {

        @SerializedName("data")
        @Expose
        private List<EntityLivestock> data = null;


        public List<EntityLivestock> getData() {
            return data;
        }

        public void setData(List<EntityLivestock> data) {
            this.data = data;
        }
}
