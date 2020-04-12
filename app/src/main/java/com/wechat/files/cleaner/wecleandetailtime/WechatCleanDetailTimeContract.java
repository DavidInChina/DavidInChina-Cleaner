package com.wechat.files.cleaner.wecleandetailtime;

import com.wechat.files.cleaner.base.BasePresenter;
import com.wechat.files.cleaner.base.BaseView;
import com.wechat.files.cleaner.data.bean.WechatFile;

import java.util.List;

public interface WechatCleanDetailTimeContract {
    interface View extends BaseView {
        void getFileGroupByTime(List<WechatFile> sevenDay, List<WechatFile> onMonth, List<WechatFile> early);
    }

    interface Presenter extends BasePresenter<View> {
        void onStart(List<WechatFile> files);
    }
}
