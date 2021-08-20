package com.theherdonline.ui.general;

import org.joda.time.DateTime;

public class SaleyardSearchFilter extends SearchFilter {
    // below is for saleyards
    String name;
    String type;
    String description;
    DateTime starts_at;
    Integer saleyard_status_id;
    Integer user_id;




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DateTime getStarts_at() {
        return starts_at;
    }

    public Integer getSaleyard_status_id() {
        return saleyard_status_id;
    }

    public void setSaleyard_status_id(Integer saleyard_status_id) {
        this.saleyard_status_id = saleyard_status_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }
}
