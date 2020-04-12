package com.wechat.files.cleaner.wecleandetail;

import com.wechat.files.cleaner.base.BasePresenter;
import com.wechat.files.cleaner.base.BaseView;
import com.wechat.files.cleaner.item.WechatCleanResultItem;

import java.util.List;

public interface WechatCleanDetailContract {
    interface View extends BaseView {
        void setCleanBtnEnabled(boolean enabled, String[] sizeString);
        void updateSizeInfo(String[] sizeInfo);
        void disableCleanBtn();
        void gotoCleanResult(String[] cleanSizeInfo);
    }

    interface Presenter extends BasePresenter<View> {
        void onCleanStart();
        void onSelectItemChanged();
        List<WechatCleanResultItem> getGroupFiles();
        void onCleanComplete(String[] cleanSizeInfo);
        void onCleanEnd();
    }
}
