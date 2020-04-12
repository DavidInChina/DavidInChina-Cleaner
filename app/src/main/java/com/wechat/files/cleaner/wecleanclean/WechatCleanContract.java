package com.wechat.files.cleaner.wecleanclean;

import com.wechat.files.cleaner.base.BasePresenter;
import com.wechat.files.cleaner.base.BaseView;
import com.wechat.files.cleaner.data.bean.WechatFileCategory;

public interface WechatCleanContract {
    interface View extends BaseView {
        void onScanComplete(WechatFileCategory category);
        void onAllScanComplete(long size);
        void updateJunkSize(long size);
    }

    interface Presenter extends BasePresenter<View> {
        void onStart();
        void stopScan();
    }
}
