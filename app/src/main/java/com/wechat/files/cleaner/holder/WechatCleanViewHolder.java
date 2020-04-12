package com.wechat.files.cleaner.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wechat.files.cleaner.R;
import com.wechat.files.cleaner.data.bean.WechatFileCategory;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WechatCleanViewHolder extends RecyclerView.ViewHolder {
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

    public WechatCleanViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        linWrapper.setOnClickListener(v -> {
            WechatFileCategory category = (WechatFileCategory) v.getTag();
            if (null != category && category.getFilesSize() > 0) {
                category.goCategoryDetail(v.getContext());
            }
        });
    }
}