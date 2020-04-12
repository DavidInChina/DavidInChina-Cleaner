package com.wechat.files.cleaner;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.wechat.files.cleaner.base.ToolBarActivity;
import com.wechat.files.cleaner.utils.MemoryInfoUtil;
import com.wechat.files.cleaner.wecleanclean.WechatCleanFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeChatCleanActivity extends ToolBarActivity {
    private static final String TAG = WeChatCleanActivity.class.getSimpleName();

    public static final int WECHAT_CLEAN_THRESHOLD_TIME = 180000;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

    }


    private void initView() {
        setContentView(R.layout.activity_wechat_clean);
        StatusBarUtil.setColor(WeChatCleanActivity.this, getResources().getColor(R.color.colorAccent));
        ButterKnife.bind(this);
        initActionBar(mToolbar, getString(R.string.app_name));
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
        showWechatCleanFragment();
    }

    private void showWechatCleanFragment() {
        WechatCleanFragment fragment = WechatCleanFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_container, fragment, WechatCleanFragment.TAG)
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
//        Intent intent = new Intent(this, CleanResultActivity.class);
//        intent.putExtra(CleanResultActivity.EXTRA_CLEAN_MODE, CleanResultActivity.CLEAN_MODE_WECHAT_CLEAN);
//        intent.putExtra(CleanResultActivity.EXTRA_JUNK_CLEAN_INFO, sizeStringArray[0] + sizeStringArray[1]);
//        startActivity(intent);
//        overridePendingTransition(R.anim.slide_in_right, android.R.anim.fade_out);
    }
}
