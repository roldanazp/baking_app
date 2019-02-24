package com.roldannanodegre.bakingapp.sqlite;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "step", primaryKeys = {"receipId","id"})
public class Step implements Serializable {

    @ColumnInfo
    private long receipId;

    @ColumnInfo
    private int id;

    @ColumnInfo
    @SerializedName("shortDescription")
    private String shortDescription;

    @ColumnInfo
    private String description;

    @ColumnInfo
    @SerializedName("videoURL")
    private String videoURL;

    @ColumnInfo
    @SerializedName("thumbnailURL")
    private String thumbnailURL;

    public long getReceipId() {
        return receipId;
    }

    public void setReceipId(long receipId) {
        this.receipId = receipId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj==null){ return false;}
        return id == ((Step) obj).id;
    }

}
