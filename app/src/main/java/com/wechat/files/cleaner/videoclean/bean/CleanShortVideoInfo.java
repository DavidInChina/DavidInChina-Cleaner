package com.wechat.files.cleaner.videoclean.bean;


import java.io.Serializable;

public class CleanShortVideoInfo implements MultiItemEntity, Serializable {
    private String fromSoure;
    private boolean isChecked;
    private long size;
    private String title;
    private long updateTime;
    private String url;
    private int videoType = 0;

    public int getVideoType() {
        return this.videoType;
    }

    public void setVideoType(int i) {
        this.videoType = i;
    }

    public boolean isChecked() {
        return this.isChecked;
    }

    public void setChecked(boolean z) {
        this.isChecked = z;
    }

    public long getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(long j) {
        this.updateTime = j;
    }

    public long getSize() {
        return this.size;
    }

    public void setSize(long j) {
        this.size = j;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String str) {
        this.url = str;
    }

    public String getFromSoure() {
        return this.fromSoure;
    }

    public void setFromSoure(String str) {
        this.fromSoure = str;
    }

    public int getItemType() {
        return 2;
    }
}
