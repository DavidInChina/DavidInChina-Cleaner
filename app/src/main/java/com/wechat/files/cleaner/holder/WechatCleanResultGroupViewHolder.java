package com.wechat.files.cleaner.holder;

import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;
import com.wechat.files.cleaner.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WechatCleanResultGroupViewHolder extends GroupViewHolder {
    @BindView(R.id.arrow)
    public ImageView mArrow;

    @BindView(R.id.junk_type_name)
    public TextView mJunkTypeName;

    @BindView(R.id.junk_count)
    public TextView mJunkItemCount;

    @BindView(R.id.total_size)
    public TextView mTotalSize;

    @BindView(R.id.checkBox)
    public AppCompatCheckBox mCheckBox;


    public WechatCleanResultGroupViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void expand() {
        animateExpand();
    }

    @Override
    public void collapse() {
        animateCollapse();
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
