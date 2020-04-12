package com.wechat.files.cleaner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jaeger.library.StatusBarUtil;
import com.wechat.files.cleaner.base.ToolBarActivity;
import com.wechat.files.cleaner.videoclean.ShortVideoActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends ToolBarActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @OnClick({R.id.tvWechat, R.id.tvShortVideo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvWechat:
                Intent intent = new Intent(this, WeChatCleanActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.tvShortVideo:
                Intent intent2 = new Intent(this, ShortVideoActivity.class);
                startActivity(intent2);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
        }
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        StatusBarUtil.setColor(MainActivity.this, getResources().getColor(R.color.colorAccent));
        ButterKnife.bind(this);
        initActionBar(mToolbar, "常用软件清理");
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }
}
