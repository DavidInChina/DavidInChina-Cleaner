package com.wechat.files.cleaner.videoclean.videos;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wechat.files.cleaner.R;
import com.wechat.files.cleaner.utils.MemoryInfoUtil;
import com.wechat.files.cleaner.videoclean.bean.CleanVideoHeadInfo;
import com.wechat.files.cleaner.videoclean.bean.MultiItemEntity;
import com.wechat.files.cleaner.videoclean.holder.VideoCleanViewHolder;
import com.wechat.files.cleaner.videoclean.utils.CleanShortVideoUtil;

import java.util.ArrayList;
import java.util.List;

public class VideoCleanAdapter extends RecyclerView.Adapter {
    private static final String TAG = VideoCleanAdapter.class.getSimpleName();
    public List<MultiItemEntity> scanInfoList = new ArrayList<>();
    private Context mContext;

    public VideoCleanAdapter(Context context) {
        scanInfoList.clear();
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new VideoCleanViewHolder(layoutInflater.inflate(R.layout.video_scan_item, parent, false));
    }

    private void updateViewText(TextView view, long size, long choosedSize, boolean appendChoose) {
        view.setTextColor(view.getContext().getResources().getColor(R.color.color_wechat_grey));
        if (size == 0) {
            view.setText(R.string.str_no_data);
        } else if (choosedSize > 0) {
            String[] selectSizeStringArray = MemoryInfoUtil.formatMemorySizeInArray(choosedSize);
            view.setTextColor(view.getContext().getResources().getColor(R.color.large_file_clean_btn));
            if (appendChoose) {
                view.setText(new StringBuilder().append(view.getContext().getString(R.string.str_wechat_selected)).append(selectSizeStringArray[0]).append(selectSizeStringArray[1]).toString());
            } else {
                view.setText(new StringBuilder().append(selectSizeStringArray[0]).append(selectSizeStringArray[1]).toString());
            }
        } else {
            String[] sizeStringArray = MemoryInfoUtil.formatMemorySizeInArray(size);
            view.setText(new StringBuilder().append(sizeStringArray[0]).append(sizeStringArray[1]).toString());
        }
    }

    private void onBindNormalViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final VideoCleanViewHolder viewHolder = (VideoCleanViewHolder) holder;
        final CleanVideoHeadInfo info = (CleanVideoHeadInfo) scanInfoList.get(position);
        viewHolder.mAppIcon.setImageResource(CleanShortVideoUtil.getCurrentDrawable(info.getSubTitle(), mContext));
        viewHolder.mTitle.setText(info.getSubTitle());
        viewHolder.mProgressBar.setVisibility(View.INVISIBLE);
        viewHolder.tvItemSize.setVisibility(View.VISIBLE);
        viewHolder.linWrapper.setTag(info);
        long size = info.getSize();
        long choosedSize = info.getSelectSize();
        updateViewText(viewHolder.tvItemSize, size, choosedSize, true);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        onBindNormalViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return scanInfoList.size();
    }

    public void clear() {
        scanInfoList.clear();
        notifyDataSetChanged();
    }

    public void removeItem(List<CleanVideoHeadInfo> scanList) {
        this.scanInfoList.removeAll(scanList);
        notifyDataSetChanged();
    }
    public void update() {
        notifyDataSetChanged();
    }

    public void onScanCompleted(List<MultiItemEntity> datas) {
        scanInfoList.clear();
        scanInfoList.addAll(datas);
        notifyDataSetChanged();
    }

//    private void sortByType(List<MultiItemEntity> list) {
//        /* bridge *//* synthetic */
//        Collections.sort(list, (obj, obj2) -> {
//            int j = obj.getCategoryType() - obj2.getCategoryType();
//            return Integer.compare(j, 0);
//        });
//    }
}
