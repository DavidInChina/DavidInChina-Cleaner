package com.wechat.files.cleaner.wecleandetailtime;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wechat.files.cleaner.R;
import com.wechat.files.cleaner.adapter.TypePageAdapter;
import com.wechat.files.cleaner.base.BaseFragment;
import com.wechat.files.cleaner.data.bean.WechatFile;
import com.wechat.files.cleaner.wecleandetail.WechatCleanDetailFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.wechat.files.cleaner.data.bean.WechatFileCategory.CATE_EMOJI;
import static com.wechat.files.cleaner.data.bean.WechatFileCategory.CATE_FILE;
import static com.wechat.files.cleaner.data.bean.WechatFileCategory.CATE_IMG;
import static com.wechat.files.cleaner.data.bean.WechatFileCategory.CATE_VIDEO;
import static com.wechat.files.cleaner.data.bean.WechatFileCategory.CATE_VOICE;

public class WechatCleanDetailTimeFragment extends BaseFragment implements
        WechatCleanDetailTimeContract.View {
    public static final String TAG = WechatCleanDetailTimeFragment.class.getSimpleName();

    private WechatCleanDetailTimeContract.Presenter mPresenter;
    @BindView(R.id.tl_theme)
    TabLayout tlFiles;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tvTips)
    TextView tvTips;

    private List<Fragment> fragments = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    private List<WechatFile> wechatFiles = new ArrayList<>();
    private int categoryType = -1;

    public WechatCleanDetailTimeFragment(List<WechatFile> files, int categoryType) {
        wechatFiles.clear();
        wechatFiles.addAll(files);
        this.categoryType = categoryType;
    }

    public WechatCleanDetailTimeFragment() {
    }

    public static WechatCleanDetailTimeFragment newInstance(List<WechatFile> files, int categoryType) {
        return new WechatCleanDetailTimeFragment(files, categoryType);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new WechatCleanDetailTimePresenter(getActivity());
        mPresenter.attachView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fgm_wechat_clean_detail, container, false);
        initView(rootView);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (null != mPresenter) {
            mPresenter.onStart(wechatFiles);
        }
    }


    private void initView(View view) {
        setUnbinder(ButterKnife.bind(this, view));
    }

    private void initData(List<WechatFile> sevenDay, List<WechatFile> onMonth, List<WechatFile> early) {
        fragments.clear();
        fragments.add(WechatCleanDetailFragment.newInstance(sevenDay, categoryType));
        fragments.add(WechatCleanDetailFragment.newInstance(onMonth, categoryType));
        fragments.add(WechatCleanDetailFragment.newInstance(early, categoryType));
        titles.clear();
        titles.add(getString(R.string.str_in_7_day));
        titles.add(getString(R.string.str_in_month));
        titles.add(getString(R.string.str_more_early));
        viewPager.setAdapter(new TypePageAdapter(getChildFragmentManager(), getContext(),
                fragments, titles));
        viewPager.setOffscreenPageLimit(2);
        if (sevenDay.size() > 0) {
            viewPager.setCurrentItem(0);
        } else if (onMonth.size() > 0) {
            viewPager.setCurrentItem(1);
        } else if (early.size() > 0) {
            viewPager.setCurrentItem(2);
        } else {
            viewPager.setCurrentItem(0);
        }
        tlFiles.setupWithViewPager(viewPager);
        tvTips.setText(getTipsResource());
    }

    private int getTipsResource() {
        switch (categoryType) {
            case CATE_VOICE:
                return R.string.str_wechat_remider_voice;
            case CATE_VIDEO:
                return R.string.str_wechat_remider_video;
            case CATE_EMOJI:
                return R.string.str_wechat_remider_emoji;
            case CATE_FILE:
                return R.string.str_wechat_remider_file;
            case CATE_IMG:
                return R.string.str_wechat_remider_img;
        }
        return 0;
    }

    @Override
    public void getFileGroupByTime(List<WechatFile> sevenDay, List<WechatFile> onMonth, List<WechatFile> early) {
        try {
            initData(sevenDay, onMonth, early);
        } catch (Exception ignored) {

        }
    }
}
