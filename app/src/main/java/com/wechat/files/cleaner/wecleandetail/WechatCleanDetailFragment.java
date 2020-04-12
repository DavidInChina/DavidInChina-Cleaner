package com.wechat.files.cleaner.wecleandetail;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.wechat.files.cleaner.R;
import com.wechat.files.cleaner.WeChatCleanDetailActivity;
import com.wechat.files.cleaner.base.BaseFragment;
import com.wechat.files.cleaner.data.bean.WechatFile;
import com.wechat.files.cleaner.event.UpdateWechat;
import com.wechat.files.cleaner.view.WechatFileDetailDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WechatCleanDetailFragment extends BaseFragment implements WechatCleanDetailContract.View, WechatCleanDetailAdapter.Callback {
    public static final String TAG = WechatCleanDetailFragment.class.getSimpleName();

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.do_junk_clean)
    TextView mJunkCleanBtn;
    @BindView(R.id.cbChooseAll)
    CheckBox cbChooseAll;

    private WechatCleanDetailContract.Presenter mPresenter;
    private WechatCleanDetailAdapter mAdapter;
    private List<WechatFile> files;
    private int categoryType;

    public WechatCleanDetailFragment(List<WechatFile> files, int categoryType) {
        this.files = files;
        this.categoryType = categoryType;
    }

    public WechatCleanDetailFragment() {
    }

    public static WechatCleanDetailFragment newInstance(List<WechatFile> files, int categoryType) {
        return new WechatCleanDetailFragment(files, categoryType);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new WechatCleanDetailPresenter(getActivity(), files, categoryType);
        mPresenter.attachView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fgm_wechat_clean_detail_result, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View view) {
        setUnbinder(ButterKnife.bind(this, view));
        mAdapter = new WechatCleanDetailAdapter(mPresenter.getGroupFiles(), categoryType);
        if (mAdapter.isFileType()) {//文件格式
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        }
        mAdapter.setCallback(this);
        mRecyclerView.setAdapter(mAdapter);
        onSelectItemChanged();
        cbChooseAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mAdapter.toggleChecked(isChecked);
            EventBus.getDefault().post(new UpdateWechat());
        });
    }

    @OnClick(R.id.do_junk_clean)
    public void onCleanClick(View view) {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.delete_selected_files)
                .setMessage(R.string.sure_to_clean_files)
                .setPositiveButton(getString(R.string.delete), (dialog, which) -> {
                    mAdapter.removeChecked();//remove item
                    if (null != mPresenter) {
                        mPresenter.onCleanStart();
                    }
                    dialog.dismiss();
                }).setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.dismiss()).create().show();
    }

    @Override
    public void onSelectItemChanged() {
        mPresenter.onSelectItemChanged();
    }

    @Override
    public void showDetailDialog(WechatFile file) {
        WechatFileDetailDialog dlg = new WechatFileDetailDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable("extra_large_file", file);
        dlg.setArguments(bundle);
        dlg.show(getFragmentManager(), TAG);
    }

    @Override
    public void setCleanBtnEnabled(boolean enabled, String[] sizeString) {
        mJunkCleanBtn.setEnabled(enabled);
        if (enabled) {
            mJunkCleanBtn.setText(getString(R.string.do_junk_clean) + " " + sizeString[0] + sizeString[1]);
        } else {
            mJunkCleanBtn.setText(getString(R.string.do_junk_clean));
        }
    }

    @Override
    public void updateSizeInfo(String[] sizeInfo) {
        mJunkCleanBtn.setText(getString(R.string.do_junk_clean) + " " + sizeInfo[0] + sizeInfo[1]);
    }

    @Override
    public void disableCleanBtn() {
        mJunkCleanBtn.setEnabled(false);
    }


    @Override
    public void gotoCleanResult(String[] cleanSizeInfo) {
        if (getActivity() != null && cleanSizeInfo != null && cleanSizeInfo.length >= 2) {
            ((WeChatCleanDetailActivity) getActivity()).showCleanResultActivity(cleanSizeInfo[0] + cleanSizeInfo[1]);
        }
    }
}
