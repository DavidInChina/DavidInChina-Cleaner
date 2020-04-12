package com.wechat.files.cleaner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.wechat.files.cleaner.adapter.TypePageAdapter;
import com.wechat.files.cleaner.base.ToolBarActivity;
import com.wechat.files.cleaner.data.WechatDataManager;
import com.wechat.files.cleaner.data.bean.WechatFileCategory;
import com.wechat.files.cleaner.utils.DeviceUtils;
import com.wechat.files.cleaner.view.ViewPagerTriangleIndicator;
import com.wechat.files.cleaner.wecleandetailtime.WechatCleanDetailTimeFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.wechat.files.cleaner.data.bean.WechatFileCategory.CATEGORY_NAME;
import static com.wechat.files.cleaner.data.bean.WechatFileCategory.CATEGORY_TYPE;

public class WeChatCleanDetailActivity extends ToolBarActivity {
    private static final String TAG = WeChatCleanDetailActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.vpti_main_tab)
    ViewPagerTriangleIndicator tlFiles;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    private String title = "";
    private String categoryType = "";
    private WechatFileCategory wechatFileCategory;
    private List<Fragment> fragments = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    private boolean showTab = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
        initView();
    }

    public static Intent getWechatCleanDetail(Context mContext) {
        return new Intent(mContext, WeChatCleanDetailActivity.class);
    }

    private void initData() {
        if (null == getIntent() || null == getIntent().getExtras()) {
            finish();
        }
        title = getIntent().getExtras().getString(CATEGORY_NAME);
        categoryType = getIntent().getExtras().getString(CATEGORY_TYPE);
        wechatFileCategory = WechatDataManager.getInstance(this).getWechatCategory(categoryType);
    }

    private void initView() {
        setContentView(R.layout.activity_wechat_clean_detail);
        StatusBarUtil.setColor(WeChatCleanDetailActivity.this, getResources().getColor(R.color.colorAccent));
        ButterKnife.bind(this);
        initActionBar(mToolbar, title);
        fragments.clear();
        fragments.add(WechatCleanDetailTimeFragment.newInstance(wechatFileCategory.getOutFiles(), wechatFileCategory.getCategoryType()));
        titles.clear();
        titles.add(getTabString(0));
        tlFiles.addView(getTabView(0));
        showTab = wechatFileCategory.getInFiles().size() > 0;
        if (showTab) {
            tlFiles.setVisibility(View.VISIBLE);
            titles.add(getTabString(1));
            tlFiles.addView(getTabView(1));
            fragments.add(WechatCleanDetailTimeFragment.newInstance(wechatFileCategory.getInFiles(), wechatFileCategory.getCategoryType()));
        } else {
            tlFiles.setVisibility(View.GONE);
        }
        viewPager.setAdapter(new TypePageAdapter(getSupportFragmentManager(), WeChatCleanDetailActivity.this,
                fragments, titles));
        //添加滑动监听
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tlFiles.scroll(position, positionOffset);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public TextView getTabView(int index) {
        TextView textView = new TextView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.
                LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 1;
        layoutParams.gravity = Gravity.CENTER;
        textView.setLayoutParams(layoutParams);
        textView.setText(getTabString(index));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(getResources().getColor(R.color.white));
        textView.setTextSize(DeviceUtils.dp2px(this, 6));
        textView.setTag(index);
        textView.setOnClickListener(v -> {
            int index1 = (int) v.getTag();
            if (index1 == 0) {
                viewPager.setCurrentItem(0);
            } else {
                viewPager.setCurrentItem(1);
            }
        });
        return textView;
    }

    public String getTabString(int index) {
        String result = "";
        switch (wechatFileCategory.getCategoryType()) {
            case WechatFileCategory.CATE_EMOJI:
                result = index == 0 ? getString(R.string.str_see_emoji) : getString(R.string.str_down_emoji);
                break;
            case WechatFileCategory.CATE_VIDEO:
                result = index == 0 ? getString(R.string.str_chat_video) : getString(R.string.str_save_video);
                break;
            case WechatFileCategory.CATE_IMG:
                result = index == 0 ? getString(R.string.str_small_img) : getString(R.string.str_big_img);
                break;
            case WechatFileCategory.CATE_FILE:
                result = index == 0 ? getString(R.string.str_wechat_files) : "";
                break;
            case WechatFileCategory.CATE_VOICE:
                result = index == 0 ? getString(R.string.str_voice_files) : "";
                break;
        }
        return result;
    }

    public void showCleanResultActivity(String cleanInfo) {

        Toast.makeText(this, "已清理:" + cleanInfo, Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(this, CleanResultActivity.class);
//        intent.putExtra(CleanResultActivity.EXTRA_CLEAN_MODE, CleanResultActivity.CLEAN_MODE_WECHAT_CLEAN);
//        intent.putExtra(CleanResultActivity.EXTRA_JUNK_CLEAN_INFO, cleanInfo);
//        startActivity(intent);
//        overridePendingTransition(R.anim.slide_in_right, android.R.anim.fade_out);
    }
}
