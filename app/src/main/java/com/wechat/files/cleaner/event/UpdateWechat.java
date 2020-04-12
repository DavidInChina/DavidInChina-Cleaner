package com.wechat.files.cleaner.event;

/**
 * author:davidinchina on 2019/5/23 14:46
 * email:davicdinchina@gmail.com
 * version:1.0.0
 * des:
 */
public class UpdateWechat {
    public boolean updateList = true;

    public UpdateWechat(boolean updateList) {
        this.updateList = updateList;
    }

    public UpdateWechat() {
    }
}
