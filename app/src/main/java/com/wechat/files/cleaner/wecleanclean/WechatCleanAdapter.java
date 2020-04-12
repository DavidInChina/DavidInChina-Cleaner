package com.wechat.files.cleaner.wecleanclean;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wechat.files.cleaner.R;
import com.wechat.files.cleaner.data.bean.WechatFileCategory;
import com.wechat.files.cleaner.event.UpdateWechat;
import com.wechat.files.cleaner.holder.WechatCleanViewHolder;
import com.wechat.files.cleaner.holder.WechatCleanjunkViewHolder;
import com.wechat.files.cleaner.utils.MemoryInfoUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static com.wechat.files.cleaner.data.bean.WechatFileCategory.CATE_CACHE;
import static com.wechat.files.cleaner.data.bean.WechatFileCategory.CATE_JUNK;
import static com.wechat.files.cleaner.data.bean.WechatFileCategory.DATA_JUNK;

public class WechatCleanAdapter extends RecyclerView.Adapter {
    private static final String TAG = WechatCleanAdapter.class.getSimpleName();
    private List<WechatFileCategory> scanInfoList = new LinkedList<>();
    private Context mContext;
    private WechatFileCategory junkCategory;
    private WechatFileCategory cacheCategory;

    public WechatCleanAdapter(Context context) {
        scanInfoList.clear();
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == DATA_JUNK) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            return new WechatCleanjunkViewHolder(layoutInflater.inflate(R.layout.wechat_scan_junk_item, parent, false));
        }
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new WechatCleanViewHolder(layoutInflater.inflate(R.layout.wechat_scan_item, parent, false));
    }

    long size;
    long choosedSize;
    long junkSize;
    long junkChoosedSize;
    long totalSize;
    long totalChoosedSize;
    boolean isHandClick = false;

    private void onBindJunkViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final WechatCleanjunkViewHolder viewHolder = (WechatCleanjunkViewHolder) holder;
        if (null != cacheCategory && null != junkCategory) {
            size = cacheCategory.getFilesSize();
            choosedSize = cacheCategory.getSelectFilesSize();
            viewHolder.mCacheCheckBox.setChecked(choosedSize > 0);
            updateViewText(viewHolder.mCacheSize, size, choosedSize, false);

            junkSize = junkCategory.getFilesSize();
            junkChoosedSize = junkCategory.getSelectFilesSize();
            updateViewText(viewHolder.mJunkSize, junkSize, junkChoosedSize, false);
            viewHolder.mJunkBox.setChecked(junkChoosedSize > 0);

            totalSize = size + junkSize;
            totalChoosedSize = choosedSize + junkChoosedSize;
            updateViewText(viewHolder.tvItemSize, totalSize, totalChoosedSize, true);
            isHandClick = false;
//            viewHolder.checkBoxTotal.setChecked(totalChoosedSize > 0);
//            viewHolder.checkBoxTotal.setOnCheckedChangeListener((buttonView, isChecked) -> {
//                if (isHandClick) {
//                    viewHolder.mJunkBox.setChecked(isChecked);
//                    viewHolder.mCacheCheckBox.setChecked(isChecked);
//                }
//                isHandClick = true;
//                totalSize = size + junkSize;
//                totalChoosedSize = choosedSize + junkChoosedSize;
//                updateViewText(viewHolder.tvItemSize, totalSize, totalChoosedSize, true);
//            });
            viewHolder.checkBoxTotal.setVisibility(View.GONE);
            if (junkSize > 0) {
                viewHolder.mJunkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    junkCategory.toggleChecked(isChecked);
                    EventBus.getDefault().post(new UpdateWechat());
                    junkSize = junkCategory.getFilesSize();
                    junkChoosedSize = junkCategory.getSelectFilesSize();
                    updateViewText(viewHolder.mJunkSize, junkSize, junkChoosedSize, false);
                });
            } else {
                viewHolder.mJunkBox.setEnabled(false);
            }
            if (size > 0) {
                viewHolder.mCacheCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    cacheCategory.toggleChecked(isChecked);
                    EventBus.getDefault().post(new UpdateWechat());
                    size = cacheCategory.getFilesSize();
                    choosedSize = cacheCategory.getSelectFilesSize();
                    updateViewText(viewHolder.mCacheSize, size, choosedSize, false);
                });
            } else {
                viewHolder.mCacheCheckBox.setEnabled(false);
            }
        }
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
        final WechatCleanViewHolder viewHolder = (WechatCleanViewHolder) holder;
        final WechatFileCategory category = scanInfoList.get(position);
        viewHolder.mAppIcon.setImageResource(category.getIconId());
        viewHolder.mTitle.setText(category.getCategoryName());
        viewHolder.mProgressBar.setVisibility(View.INVISIBLE);
        viewHolder.tvItemSize.setVisibility(View.VISIBLE);
        viewHolder.linWrapper.setTag(category);
        long size = category.getFilesSize();
        long choosedSize = category.getSelectFilesSize();
        updateViewText(viewHolder.tvItemSize, size, choosedSize, true);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == DATA_JUNK) {
            onBindJunkViewHolder(holder, position);
        } else {
            onBindNormalViewHolder(holder, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return scanInfoList.get(position).dataType;
    }

    @Override
    public int getItemCount() {
        return scanInfoList.size();
    }

    public void clear() {
        scanInfoList.clear();
        notifyDataSetChanged();
    }

    public void update() {
        notifyDataSetChanged();
    }

    public void onScanCompleted(WechatFileCategory category) {
        synchronized (WechatCleanAdapter.class) {
            if (category.getCategoryType() == CATE_JUNK) {
                junkCategory = category;
            } else if (category.getCategoryType() == CATE_CACHE) {
                cacheCategory = category;
            } else {
                scanInfoList.add(category);
                sortByType(scanInfoList);
                notifyDataSetChanged();
            }
            if (null != junkCategory && null != cacheCategory && scanInfoList.get(0).dataType != DATA_JUNK) {
                WechatFileCategory category1 = new WechatFileCategory(CATE_JUNK, mContext.getString(R.string.str_wechat_cache), R.mipmap.ic_chat_cache);
                category1.dataType = DATA_JUNK;
                scanInfoList.add(0, category1);
                sortByType(scanInfoList);
                notifyDataSetChanged();
            }
        }
    }

    private void sortByType(List<WechatFileCategory> list) {
        /* bridge *//* synthetic */
        Collections.sort(list, (obj, obj2) -> {
            int j = obj.getCategoryType() - obj2.getCategoryType();
            return Integer.compare(j, 0);
        });
    }
}
