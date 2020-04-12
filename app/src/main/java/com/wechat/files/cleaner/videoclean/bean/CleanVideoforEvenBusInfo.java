package com.wechat.files.cleaner.videoclean.bean;

public class CleanVideoforEvenBusInfo {
    private long videoSize;
    private int videoType;

    public int getVideoType() {
        return this.videoType;
    }

    public void setVideoType(int i) {
        this.videoType = i;
    }

    public long getVideoSize() {
        return this.videoSize;
    }

    public void setVideoSize(long j) {
        this.videoSize = j;
    }
}
