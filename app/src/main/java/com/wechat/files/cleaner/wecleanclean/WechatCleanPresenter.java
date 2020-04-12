package com.wechat.files.cleaner.wecleanclean;

import android.content.Context;

import com.wechat.files.cleaner.data.WechatDataManager;
import com.wechat.files.cleaner.data.bean.WechatFileCategory;


public class WechatCleanPresenter implements WechatCleanContract.Presenter, WechatDataManager.WechatCategoryListener {
    private static final String TAG = WechatCleanPresenter.class.getSimpleName();

    private WechatCleanContract.View mView;
    private Context mContext;
    private  WechatDataManager.WechatCategoryListener categoryListener;
    private long totalSize = 0;

    public WechatCleanPresenter(Context context) {
        mContext = context;
        categoryListener = this;
    }

    @Override
    public void attachView(WechatCleanContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    public void onStart() {
        getWechatData();
        totalSize = 0;
    }

    @Override
    public void onWechatInfoSizeUpdate(long size) {
        synchronized (WechatCleanPresenter.class) {
            totalSize += size;
            if (null != mView) {
                mView.updateJunkSize(totalSize);
            }
        }
    }

    @Override
    public void onWechatInfoSizeSucceed(long size) {
        if (null != mView) {
            mView.onAllScanComplete(size);
        }
    }

    @Override
    public void getWechatCategory(WechatFileCategory category) {
        if (null != mView) {
            mView.onScanComplete(category);
        }
    }

    private void getWechatData() {
        WechatDataManager.getInstance(mContext).initData(new WechatDataManager.InitDataListener() {
            @Override
            public void initDataFinish() {
                WechatDataManager.getInstance(mContext.getApplicationContext()).
                        getWechatCategory(categoryListener);
            }
        });
    }


    public void stopScan() {

    }
}
