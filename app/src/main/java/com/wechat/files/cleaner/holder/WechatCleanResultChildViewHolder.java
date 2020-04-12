package com.wechat.files.cleaner.holder;

import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.thoughtbot.expandablecheckrecyclerview.viewholders.CheckableChildViewHolder;
import com.wechat.files.cleaner.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WechatCleanResultChildViewHolder extends CheckableChildViewHolder {
    @BindView(R.id.junk_icon)
    public ImageView mJunkIcon;

    @BindView(R.id.junk_name)
    public TextView mJunkName;

    @BindView(R.id.junk_size)
    public TextView mJunkSize;

    @BindView(R.id.checkBox)
    public AppCompatCheckBox mCheckBox;

    @BindView(R.id.flWrapper)
    public FrameLayout flWrapper;


    public WechatCleanResultChildViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public Checkable getCheckable() {
        return mCheckBox;
    }
}
