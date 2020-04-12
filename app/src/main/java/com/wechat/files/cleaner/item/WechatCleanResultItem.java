package com.wechat.files.cleaner.item;

import com.wechat.files.cleaner.data.bean.WechatFile;
import com.thoughtbot.expandablecheckrecyclerview.models.MultiCheckExpandableGroup;

import java.util.List;


public class WechatCleanResultItem extends MultiCheckExpandableGroup {
    public final List<WechatFile> mJunks;
    public boolean mExpanded = true;

    public WechatCleanResultItem(String title, List<WechatFile> junks) {
        super(title, junks);
        this.mJunks = junks;
    }

    public long getTotalSize() {
        long totalSize = 0;
        for (WechatFile baseJunkInfo : mJunks) {
            totalSize += baseJunkInfo.getFileSize();
        }
        return totalSize;
    }

    public void removeItem(WechatFile file) {
        if (null != mJunks) {
            mJunks.remove(file);
        }

    }
    public boolean allSelected() {
        boolean selected = true;
        for (WechatFile baseJunkInfo : mJunks) {
            if (!baseJunkInfo.isChecked()) {
                selected = false;
                break;
            }
        }
        return selected;
    }

    public boolean isExpanded() {
        return mExpanded;
    }

    public void setExpanded(boolean expanded) {
        this.mExpanded = expanded;
    }
}
