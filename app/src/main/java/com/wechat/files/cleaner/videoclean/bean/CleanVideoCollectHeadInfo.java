package com.wechat.files.cleaner.videoclean.bean;


public class CleanVideoCollectHeadInfo implements MultiItemEntity {
    private int Days;
    private String stringYear;

    public String getStringYear() {
        return this.stringYear;
    }

    public void setStringYear(String str) {
        this.stringYear = str;
    }

    public int getDays() {
        return this.Days;
    }

    public void setDays(int i) {
        this.Days = i;
    }

    public String toString() {
        return "CleanWxItemInfo{, Days=" + this.Days + '}';
    }

    public int getItemType() {
        return 1;
    }
}
