package com.wechat.files.cleaner.holder;

import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wechat.files.cleaner.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WechatCleanjunkViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.cache_size)
    public TextView mCacheSize;

    @BindView(R.id.junk_size)
    public TextView mJunkSize;

    @BindView(R.id.tvItemSize)
    public TextView tvItemSize;

    @BindView(R.id.checkBox)
    public AppCompatCheckBox mJunkBox;


    @BindView(R.id.cacheCheckBox)
    public AppCompatCheckBox mCacheCheckBox;

    @BindView(R.id.checkBoxTotal)
    public AppCompatCheckBox checkBoxTotal;

    @BindView(R.id.linWrapper)
    public LinearLayout linWrapper;

    @BindView(R.id.flCache)
    public FrameLayout flCache;

    @BindView(R.id.flJunk)
    public FrameLayout flJunk;

    @BindView(R.id.arrow)
    public ImageView mArrow;

    public WechatCleanjunkViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        //点击切换
        linWrapper.setOnClickListener(view -> {
            if (flCache.getVisibility() == View.VISIBLE) {
                flCache.setVisibility(View.GONE);
                flJunk.setVisibility(View.GONE);
                animateCollapse();
            } else {
                flCache.setVisibility(View.VISIBLE);
                flJunk.setVisibility(View.VISIBLE);
                animateExpand();
            }
        });
    }

    private void animateExpand() {
        RotateAnimation rotate =
                new RotateAnimation(360, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        mArrow.startAnimation(rotate);
    }

    private void animateCollapse() {
        RotateAnimation rotate =
                new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        mArrow.startAnimation(rotate);
    }
}