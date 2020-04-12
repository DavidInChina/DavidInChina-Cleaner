package com.wechat.files.cleaner.videoclean.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wechat.files.cleaner.R;
import com.wechat.files.cleaner.videoclean.bean.CleanVideoHeadInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoCleanViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.icon)
    public ImageView mAppIcon;

    @BindView(R.id.title)
    public TextView mTitle;

    @BindView(R.id.tvItemSize)
    public TextView tvItemSize;

    @BindView(R.id.progress)
    public ProgressBar mProgressBar;

    @BindView(R.id.linWrapper)
    public LinearLayout linWrapper;

    public VideoCleanViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        linWrapper.setOnClickListener(v -> {
            CleanVideoHeadInfo info = (CleanVideoHeadInfo) v.getTag();
            if (null != info && info.getSize() > 0) {
//                category.goCategoryDetail(v.getContext());
            }
        });
    }
}