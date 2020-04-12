package com.wechat.files.cleaner;

import android.app.Application;
import android.content.Context;

import com.wechat.files.cleaner.utils.PrefsCleanUtil;

/**
 * author:davidinchina on 2019/5/8 11:00
 * email:davicdinchina@gmail.com
 * version:1.0.0
 * des:
 */
public class WechatCleannerApp extends Application {
    public static Context AppContext;
    @Override
    public void onCreate() {
        super.onCreate();
        AppContext = this;
        PrefsCleanUtil.init(this, getPackageName() + "_preference", 0);
    }
}
