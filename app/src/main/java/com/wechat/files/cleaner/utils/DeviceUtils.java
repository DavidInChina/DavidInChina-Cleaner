package com.wechat.files.cleaner.utils;

import android.content.Context;

/**
 * Created by ShengWang on 2018/10/10.
 */

public class DeviceUtils {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
