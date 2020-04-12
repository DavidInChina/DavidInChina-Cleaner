package com.wechat.files.cleaner.wecleandetailtime;

import android.content.Context;

import com.wechat.files.cleaner.data.bean.WechatFile;

import java.util.ArrayList;
import java.util.List;

public class WechatCleanDetailTimePresenter implements WechatCleanDetailTimeContract.Presenter {


    private WechatCleanDetailTimeContract.View mView;
    private Context mContext;

    public WechatCleanDetailTimePresenter(Context context) {
        mContext = context;
    }


    @Override
    public void onStart(List<WechatFile> files) {
        List<WechatFile> sevenFiles = new ArrayList<>();
        List<WechatFile> monthFiles = new ArrayList<>();
        List<WechatFile> earlyFiles = new ArrayList<>();
        long sevenDay = 7 * 24 * 60 * 60 * 1000L;//one week
        long month = 30 * 24 * 60 * 60 * 1000L;//one month
        for (WechatFile file : files
        ) {
            if (isInTime(sevenDay, file.getModifyTime())) {
                sevenFiles.add(file);
            } else if (isInTime(month, file.getModifyTime())) {
                monthFiles.add(file);
            } else {
                earlyFiles.add(file);
            }
        }
        if (null != mView) {
            mView.getFileGroupByTime(sevenFiles, monthFiles, earlyFiles);
        }
    }

    public boolean isInTime(long time, long fileTime) {
        return System.currentTimeMillis() - fileTime < time;
    }

    @Override
    public void attachView(WechatCleanDetailTimeContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }
}
