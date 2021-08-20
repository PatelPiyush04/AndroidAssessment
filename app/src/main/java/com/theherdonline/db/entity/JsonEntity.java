package com.theherdonline.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "tableJson")
public class JsonEntity {
    @PrimaryKey
    Integer id;
    String jsonString;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getJsonString() {
        return jsonString;
    }

    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
    }

    public JsonEntity(Integer id, String jsonString) {
        this.id = id;
        this.jsonString = jsonString;
    }

    @Override
    public String toString() {
        return jsonString;
    }
}
