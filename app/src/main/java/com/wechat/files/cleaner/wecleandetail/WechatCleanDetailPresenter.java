package com.wechat.files.cleaner.wecleandetail;

import android.content.Context;

import com.wechat.files.cleaner.data.WechatDataManager;
import com.wechat.files.cleaner.data.bean.WechatFile;
import com.wechat.files.cleaner.data.utils.FileUtils;
import com.wechat.files.cleaner.event.UpdateWechat;
import com.wechat.files.cleaner.item.WechatCleanResultItem;
import com.wechat.files.cleaner.utils.MemoryInfoUtil;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class WechatCleanDetailPresenter implements WechatCleanDetailContract.Presenter {

    private WechatCleanDetailContract.View mView;
    private final CompositeDisposable mCompositeDisposable;
    private Context mContext;
    private List<WechatCleanResultItem> mGroupJunks;
    private List<WechatFile> mFiles;
    private int categoryType;

    public WechatCleanDetailPresenter(Context context, List<WechatFile> mFiles, int categoryType) {
        mCompositeDisposable = new CompositeDisposable();
        mContext = context.getApplicationContext();
        this.mFiles = mFiles;
        this.categoryType = categoryType;
        initGroupJunks();
    }

    private void initGroupJunks() {
        mGroupJunks = new LinkedList<>();
        List<WechatFile> groupFiles = new ArrayList<>();
        if (null != mFiles) {
            int currentDay = -1;
            for (WechatFile file : mFiles
            ) {
                Date date = new Date(file.getModifyTime());
                if (currentDay != date.getDay()) {
                    if (currentDay != -1) {
                        addGroupFiles(groupFiles);
                    }
                    groupFiles.clear();
                    currentDay = date.getDay();
                }
                groupFiles.add(file);
            }
            if (groupFiles.size() > 0) {
                addGroupFiles(groupFiles);
                groupFiles.clear();
            }
        }
    }

    private void addGroupFiles(List<WechatFile> groupFiles) {
        List<WechatFile> addFiles = new ArrayList<>();
        addFiles.addAll(groupFiles);
        Date date = new Date(groupFiles.get(0).getModifyTime());
        mGroupJunks.add(new WechatCleanResultItem(getDateDay(date),
                addFiles));
    }

    private String getDateDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        String[] str1 = new String[]{"Error", "日", "一", "二", "三", "四", "五", "六"};
        calendar.setTime(date);
        return simpleDateFormat.format(date) + "    周" + str1[calendar.get(Calendar.DAY_OF_WEEK)];
    }

    @Override
    public void onSelectItemChanged() {
        long selSize = FileUtils.scannerFilesCheckedSize(mFiles);
        String[] sizeStringArray = MemoryInfoUtil.formatMemorySizeInArray(selSize);
        if (null != mView) {
            if (selSize > 0) {
                mView.setCleanBtnEnabled(true, sizeStringArray);
            } else {
                mView.setCleanBtnEnabled(false, null);
            }
            mView.updateSizeInfo(sizeStringArray);
        }
    }

    @Override
    public List<WechatCleanResultItem> getGroupFiles() {
        return mGroupJunks;
    }

    @Override
    public void onCleanStart() {
        if (null != mView) {
            mView.disableCleanBtn();
        }
        mCompositeDisposable.add(Observable.create((ObservableOnSubscribe<String[]>) emitter -> {
                    long selSize = FileUtils.scannerFilesCheckedSize(mFiles);
                    WechatDataManager.getInstance(mContext).deleteWechatFiles(mFiles, categoryType);//删除物理文件
                    FileUtils.removeSelectFiles(mFiles);//删除内存数据
                    EventBus.getDefault().post(new UpdateWechat());
                    String[] sizeStringArray = MemoryInfoUtil.formatMemorySizeInArray(selSize);
                    emitter.onNext(sizeStringArray);
                })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::onCleanComplete
                        )
        );
    }

    @Override
    public void onCleanComplete(String[] cleanSizeInfo) {
        if (null != mView) {
            mView.gotoCleanResult(cleanSizeInfo);
        }
    }

    @Override
    public void onCleanEnd() {

    }

    @Override
    public void attachView(WechatCleanDetailContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mCompositeDisposable.dispose();
        mView = null;
    }
}
