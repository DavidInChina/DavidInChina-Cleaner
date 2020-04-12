package com.wechat.files.cleaner.wecleanclean;


import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wechat.files.cleaner.R;
import com.wechat.files.cleaner.WeChatCleanActivity;
import com.wechat.files.cleaner.base.BaseFragment;
import com.wechat.files.cleaner.data.WechatDataManager;
import com.wechat.files.cleaner.data.bean.WechatFileCategory;
import com.wechat.files.cleaner.event.UpdateWechat;
import com.wechat.files.cleaner.utils.DeviceUtils;
import com.wechat.files.cleaner.utils.MemoryInfoUtil;
import com.wechat.files.cleaner.utils.PackageUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WechatCleanFragment extends BaseFragment implements WechatCleanContract.View {
    public static final String TAG = WechatCleanFragment.class.getSimpleName();

    @BindView(R.id.junk_size)
    TextView mJunkSizeTextView;

    @BindView(R.id.junk_size_unit)
    TextView mJunkSizeUnitTextView;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.do_junk_clean)
    TextView mDoClean;

    private WechatCleanContract.Presenter mPresenter;
    private WechatCleanAdapter mJunkScanAdapter;

    public WechatCleanFragment() {
    }

    public static WechatCleanFragment newInstance() {
        return new WechatCleanFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new WechatCleanPresenter(getActivity());
        mPresenter.attachView(this);
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
        if (null != mPresenter) {
            mPresenter.onStart();
        }
        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();
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
                        onAllScanComplete(WechatDataManager.getInstance(getContext()).getCategoryTotalSize());
                        if (getActivity() != null) {
                            ((WeChatCleanActivity) getActivity()).showCleanResultActivity(selectedSize);
                        }
                    }
                    dialog.dismiss();
                }).setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.dismiss()).create().show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UpdateWechat event) {
        if (null != mJunkScanAdapter && null != mRecyclerView) {
            if (event.updateList) {
                if (mRecyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE || !mRecyclerView.isComputingLayout()) {
                    mJunkScanAdapter.update();
                }
            }
            onAllScanComplete(WechatDataManager.getInstance(getContext()).getCategoryTotalSize());
        }
    }

    @Override
    public void onDestroy() {
        if (null != mPresenter) {
            mPresenter.stopScan();
            mPresenter.detachView();
        }
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
        mJunkScanAdapter = new WechatCleanAdapter(getContext());
        mRecyclerView.setAdapter(mJunkScanAdapter);
    }

    @Override
    public void onScanComplete(WechatFileCategory category) {
        if (mRecyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE || (!mRecyclerView.isComputingLayout())) {
            Activity activity = getActivity();
            if (null != activity && !activity.isFinishing() && null != mJunkScanAdapter) {
                activity.runOnUiThread(() -> mJunkScanAdapter.onScanCompleted(category));
            }
        }
    }

    @Override
    public void onAllScanComplete(long size) {
        WeChatCleanActivity activity = (WeChatCleanActivity) getActivity();
        if (!PackageUtils.activityNotAvailable(activity)) {
            activity.onWechatJunkScanAllComplete(size);
        }
        updateJunkSize(size);
        if (null != mDoClean && isAdded()) {
            long selectedSize = WechatDataManager.getInstance(getContext()).getCategorySelectedSize();
            if (selectedSize > 0) {
                String[] sizeStringArray = MemoryInfoUtil.formatMemorySizeInArray(selectedSize);
                getActivity().runOnUiThread(() -> {
                    mDoClean.setEnabled(true);
                    mDoClean.setText(getString(R.string.do_junk_clean) + sizeStringArray[0] + sizeStringArray[1]);
                });
            } else {
                getActivity().runOnUiThread(() -> {
                    mDoClean.setEnabled(false);
                    mDoClean.setText(getString(R.string.do_junk_clean));
                });
            }
        }
    }

    @Override
    public void updateJunkSize(long size) {
        String[] sizeStringArray = MemoryInfoUtil.formatMemorySizeInArray(size);
        Activity activity = getActivity();
        if (null != activity && !activity.isFinishing()) {
            activity.runOnUiThread(() -> {
                if (null != mJunkSizeTextView && null != mJunkSizeUnitTextView) {
                    mJunkSizeTextView.setText(sizeStringArray[0]);
                    mJunkSizeUnitTextView.setText(sizeStringArray[1]);
                }
            });
        }
    }
}
