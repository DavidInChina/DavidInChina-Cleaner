package com.wechat.files.cleaner.videoclean.videos;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wechat.files.cleaner.R;
import com.wechat.files.cleaner.base.BaseFragment;
import com.wechat.files.cleaner.data.WechatDataManager;
import com.wechat.files.cleaner.event.UpdateWechat;
import com.wechat.files.cleaner.utils.DeviceUtils;
import com.wechat.files.cleaner.utils.MemoryInfoUtil;
import com.wechat.files.cleaner.utils.PrefsCleanUtil;
import com.wechat.files.cleaner.videoclean.MusicLoader;
import com.wechat.files.cleaner.videoclean.ShortVideoActivity;
import com.wechat.files.cleaner.videoclean.bean.CleanShortVideoInfo;
import com.wechat.files.cleaner.videoclean.bean.CleanVideoHeadInfo;
import com.wechat.files.cleaner.videoclean.bean.MultiItemEntity;
import com.wechat.files.cleaner.videoclean.utils.SdUtils;
import com.wechat.files.cleaner.videoclean.utils.ThreadTaskUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.wechat.files.cleaner.videoclean.MusicLoader.CLEAN_VIDEO_TOTAL_SIZE;

public class VideoCleanFragment extends BaseFragment {
    public static final String TAG = VideoCleanFragment.class.getSimpleName();
    public static final String CLEAN_SD_URI = "clean_sd_uri";

    @BindView(R.id.junk_size)
    TextView mJunkSizeTextView;

    @BindView(R.id.junk_size_unit)
    TextView mJunkSizeUnitTextView;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.do_junk_clean)
    TextView mDoClean;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            long totalSize = msg.getData().getLong("totalSize");
            String[] sizeStringArray = MemoryInfoUtil.formatMemorySizeInArray(totalSize);
            if (null != mJunkSizeTextView && null != mJunkSizeUnitTextView) {
                mJunkSizeTextView.setText(sizeStringArray[0]);
                mJunkSizeUnitTextView.setText(sizeStringArray[1]);
            }
        }
    };

    public static ArrayList<MultiItemEntity> dataList = new ArrayList();
    private long totalSize;
    private List<CleanShortVideoInfo> shortVideoInfos = new ArrayList();

    private VideoCleanAdapter mJunkScanAdapter;

    public VideoCleanFragment() {
    }

    public static VideoCleanFragment newInstance() {
        return new VideoCleanFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fgm_wechat_scan, container, false);
        EventBus.getDefault().register(this);
        initView(rootView);
        if (null != mJunkScanAdapter) {
            mJunkScanAdapter.clear();
        }
        return rootView;
    }


    @OnClick(R.id.do_junk_clean)
    public void onCleanClick(View view) {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.delete_selected_files)
                .setMessage(R.string.sure_to_clean_files)
                .setPositiveButton(getString(R.string.delete), (dialog, which) -> {
                    long selectedSize = WechatDataManager.getInstance(getContext()).getCategorySelectedSize();
                    WechatDataManager.getInstance(getContext()).deleteAllSelectedWechatFiles();
                    if (null != mJunkScanAdapter && null != mRecyclerView) {
                        if (mRecyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE || !mRecyclerView.isComputingLayout()) {
                            mJunkScanAdapter.update();
                        }
//                        onAllScanComplete(WechatDataManager.getInstance(getContext()).getCategoryTotalSize());
                        if (getActivity() != null) {
                            ((ShortVideoActivity) getActivity()).showCleanResultActivity(selectedSize);
                        }
                    }
                    dialog.dismiss();
                }).setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.dismiss()).create().show();
    }

    private void deleteData() {
        int i = 0;
        this.shortVideoInfos.clear();
        List<CleanVideoHeadInfo> arrayList = new ArrayList();
        int i2 = 0;
        while (i2 < dataList.size()) {
            if (dataList.get(i2) instanceof CleanVideoHeadInfo) {
                CleanVideoHeadInfo cleanVideoHeadInfo = (CleanVideoHeadInfo) dataList.get(i2);
                if (cleanVideoHeadInfo.getSubItems() != null) {
                    int i3 = 0;
                    while (i3 < cleanVideoHeadInfo.getSubItems().size()) {
                        //删除单个子项
                        if (cleanVideoHeadInfo.getSubItems().get(i3) != null && cleanVideoHeadInfo.getSubItems().get(i3).isChecked()) {
                            this.shortVideoInfos.add(cleanVideoHeadInfo.getSubItems().get(i3));
                            cleanVideoHeadInfo.setSize(cleanVideoHeadInfo.getSize() - cleanVideoHeadInfo.getSubItems().get(i3).getSize());
                            cleanVideoHeadInfo.setSelectSize(cleanVideoHeadInfo.getSelectSize() - cleanVideoHeadInfo.getSubItems().get(i3).getSize());
                            cleanVideoHeadInfo.removeSubItem(i3);
                            if (cleanVideoHeadInfo.isExpanded()) {
                                dataList.remove((i3 + i2) + 1);
                            }
                            i3--;
                        }
                        i3++;
                    }
                }
                //删除大类
                if (cleanVideoHeadInfo.isChecked()) {
                    if (this.mJunkScanAdapter.scanInfoList.size() > 0) {
                        Iterator it = this.mJunkScanAdapter.scanInfoList.iterator();
                        while (it.hasNext()) {
                            CleanVideoHeadInfo cleanShortVideoListInfo = (CleanVideoHeadInfo) it.next();
                            if (!TextUtils.isEmpty(cleanVideoHeadInfo.getSubTitle()) && cleanVideoHeadInfo.getSubTitle().equals(cleanShortVideoListInfo.getSubTitle())) {
                                arrayList.add(cleanShortVideoListInfo);
                                it.remove();
                            }
                        }
                    }
                    dataList.remove(i2);
                    i2--;
                }
            }
            i2++;
        }
        this.mJunkScanAdapter.removeItem(arrayList);
        if (this.shortVideoInfos != null && this.shortVideoInfos.size() > 0) {
            while (i < this.shortVideoInfos.size()) {
                try {
                    PrefsCleanUtil.getInstance().putLong(CLEAN_VIDEO_TOTAL_SIZE, PrefsCleanUtil.getInstance().getLong(CLEAN_VIDEO_TOTAL_SIZE) - this.shortVideoInfos.get(i).getSize());
                    if (new File(this.shortVideoInfos.get(i).getUrl()).exists()) {
                        deleteOnSdCardOrOnPhone(this.shortVideoInfos.get(i));
                    }
                    Message obtain = Message.obtain();
                    obtain.what = 44;
                    obtain.obj = this.shortVideoInfos.get(i).getSize();
//                    this.f.sendMessage(obtain);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                i++;
            }
        }
    }

    public void deleteOnSdCardOrOnPhone(CleanShortVideoInfo cleanShortVideoInfo) {
        if (cleanShortVideoInfo == null) {
            return;
        }
        if (Build.VERSION.SDK_INT < 21 || !cleanShortVideoInfo.getUrl().contains("sdcard1")) {
            new File(cleanShortVideoInfo.getUrl()).delete();
            getActivity().sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.parse("file://" + cleanShortVideoInfo.getUrl())));
            return;
        }
        String string = PrefsCleanUtil.getInstance().getString(CLEAN_SD_URI);
        if (!SdUtils.deleteFiles(new File(cleanShortVideoInfo.getUrl()), Uri.parse(string), getActivity())) {
            Toast.makeText(getActivity(), getString(R.string.delete_false), Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UpdateWechat event) {
        if (null != mJunkScanAdapter && null != mRecyclerView) {
            if (event.updateList) {
                if (mRecyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE || !mRecyclerView.isComputingLayout()) {
                    mJunkScanAdapter.update();
                }
            }
//            onAllScanComplete(WechatDataManager.getInstance(getContext()).getCategoryTotalSize());
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void initView(View view) {
        setUnbinder(ButterKnife.bind(this, view));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                       RecyclerView.State state) {
                int itemSpace = 2;
                super.getItemOffsets(outRect, view, parent, state);
                int pos = parent.getChildAdapterPosition(view);
                int count = parent.getAdapter().getItemCount();
                outRect.top = (pos == 0) ? 2 * itemSpace : itemSpace;
                outRect.bottom = (pos == (count - 1)) ? 2 * itemSpace + DeviceUtils.dp2px(getContext(), 60) : itemSpace;
                outRect.left = outRect.right = 2 * itemSpace;
            }
        });
        mJunkScanAdapter = new VideoCleanAdapter(getContext());
        mRecyclerView.setAdapter(mJunkScanAdapter);
        getData();
    }

    private void getData() {
        ThreadTaskUtil.executeNormalTask("-CleanShortVideoFragment-getAllShortVideoList-461--", () -> {
            MusicLoader musicLoader = new MusicLoader();
            musicLoader.setMusicLoaderLinstener(cleanVideoforEvenBusInfo -> {
                totalSize = totalSize + cleanVideoforEvenBusInfo.getVideoSize();
                Message message = new Message();
                message.what = 0;
                Bundle bundle = new Bundle();
                bundle.putLong("totalSize", totalSize);
                message.setData(bundle);
                handler.sendMessage(message);
            });
            getVideosGroup(musicLoader.getShortVideoList(), dataList);
            if (mRecyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE || (!mRecyclerView.isComputingLayout())) {
                Activity activity = getActivity();
                if (null != activity && !activity.isFinishing() && null != mJunkScanAdapter) {
                    activity.runOnUiThread(() -> mJunkScanAdapter.onScanCompleted(dataList));
                }
            }
        });
    }

    private void getVideosGroup(List<CleanShortVideoInfo> list, ArrayList<MultiItemEntity> arrayList) {
        HashMap hashMap = new HashMap();
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= list.size()) {
                break;
            }
            if (!TextUtils.isEmpty(list.get(i2).getFromSoure())) {
                CleanVideoHeadInfo cleanVideoHeadInfo;
                if (hashMap.get(list.get(i2).getFromSoure()) == null) {
                    cleanVideoHeadInfo = new CleanVideoHeadInfo();
                } else {
                    cleanVideoHeadInfo = (CleanVideoHeadInfo) hashMap.get(list.get(i2).getFromSoure());
                }
                cleanVideoHeadInfo.setChecked(true);
                list.get(i2).setChecked(true);
                cleanVideoHeadInfo.addSubItem(list.get(i2));
                cleanVideoHeadInfo.setSelectImgUrl(list.get(i2).getUrl());
                cleanVideoHeadInfo.setSize(cleanVideoHeadInfo.getSize() + list.get(i2).getSize());
                cleanVideoHeadInfo.setSelectSize(cleanVideoHeadInfo.getSelectSize() + list.get(i2).getSize());
                cleanVideoHeadInfo.setSubTitle(list.get(i2).getFromSoure());
                hashMap.put(list.get(i2).getFromSoure(), cleanVideoHeadInfo);
            }
            i = i2 + 1;
        }
        if (hashMap.size() > 0) {
            for (Object str : hashMap.keySet()) {
                if (!(((CleanVideoHeadInfo) hashMap.get(str)).getSubItems() == null || ((CleanVideoHeadInfo) hashMap.get(str)).getSubItems().size() <= 0 || arrayList.contains(hashMap.get(str)))) {
                    arrayList.add((MultiItemEntity) hashMap.get(str));
                }
            }
        }
        hashMap.clear();
        list.clear();
    }
}
