package com.wechat.files.cleaner.videoclean.bean;

import android.animation.ValueAnimator;


public class CleanVideoHeadInfo extends AbstractExpandableItem<CleanShortVideoInfo> implements MultiItemEntity {
    public static final int ANIM_STATUS_STOP = 0;
    public static final int ANIM_STATUS_TRIGGER_DOWN = 2;
    public static final int ANIM_STATUS_TRIGGER_UP = 1;
    private int animStatus;
    private ValueAnimator downAnimator;
    private boolean isChecked;
    private String selectImgUrl;
    private long selectSize;
    private long size;
    private String subTitle;
    private boolean textColor;
    private String title;
    private ValueAnimator upAnimator;

    public String getSelectImgUrl() {
        return this.selectImgUrl;
    }

    public void setSelectImgUrl(String str) {
        this.selectImgUrl = str;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String getSubTitle() {
        return this.subTitle;
    }

    public void setSubTitle(String str) {
        this.subTitle = str;
    }

    public boolean isChecked() {
        return this.isChecked;
    }

    public void setChecked(boolean z) {
        this.isChecked = z;
    }

    public long getSize() {
        return this.size;
    }

    public void setSize(long j) {
        this.size = j;
    }

    public boolean isTextColor() {
        return this.textColor;
    }

    public void setTextColor(boolean z) {
        this.textColor = z;
    }

    public long getSelectSize() {
        return this.selectSize;
    }

    public void setSelectSize(long j) {
        this.selectSize = j;
    }

    public int getLevel() {
        return 0;
    }

    public int getItemType() {
        return 1;
    }

    public int getAnimStatus() {
        return this.animStatus;
    }

    public void setAnimStatus(int i) {
        this.animStatus = i;
    }

    public ValueAnimator getUpAnimator() {
        return this.upAnimator;
    }

    public void setUpAnimator(ValueAnimator valueAnimator) {
        this.upAnimator = valueAnimator;
    }

    public ValueAnimator getDownAnimator() {
        return this.downAnimator;
    }

    public void setDownAnimator(ValueAnimator valueAnimator) {
        this.downAnimator = valueAnimator;
    }
}
