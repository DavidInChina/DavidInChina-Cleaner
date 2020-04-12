package com.wechat.files.cleaner.videoclean;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.wechat.files.cleaner.R;
import com.wechat.files.cleaner.base.ToolBarActivity;
import com.wechat.files.cleaner.utils.MemoryInfoUtil;
import com.wechat.files.cleaner.videoclean.videos.VideoCleanFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShortVideoActivity extends ToolBarActivity {
    private static final String TAG = ShortVideoActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void showVideoCleanFragment() {
        VideoCleanFragment fragment = VideoCleanFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_container, fragment, VideoCleanFragment.TAG)
                .commit();
    }

    public void onWechatJunkScanAllComplete(long size) {
        if (0L == size) {
            showCleanResultActivity(size);
        }
    }

    public void showCleanResultActivity(long size) {
        if (size <= 0) {
            return;
        }
        String[] sizeStringArray = MemoryInfoUtil.formatMemorySizeInArray(size);
        Toast.makeText(this, "已清理:" + sizeStringArray[0] + sizeStringArray[1], Toast.LENGTH_SHORT).show();
    }

    private void initView() {
        setContentView(R.layout.activity_short_video);
        StatusBarUtil.setColor(ShortVideoActivity.this, getResources().getColor(R.color.colorAccent));
        ButterKnife.bind(this);
        initActionBar(mToolbar, "常用软件清理");
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        showVideoCleanFragment();
    }

}
