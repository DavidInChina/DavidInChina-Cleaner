package com.wechat.files.cleaner.wecleandetail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.thoughtbot.expandablecheckrecyclerview.CheckableChildRecyclerViewAdapter;
import com.thoughtbot.expandablecheckrecyclerview.models.CheckedExpandableGroup;
import com.thoughtbot.expandablerecyclerview.listeners.GroupExpandCollapseListener;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.models.ExpandableListPosition;
import com.wechat.files.cleaner.R;
import com.wechat.files.cleaner.data.bean.WechatFile;
import com.wechat.files.cleaner.event.UpdateWechat;
import com.wechat.files.cleaner.holder.WechatCleanResultChildViewHolder;
import com.wechat.files.cleaner.holder.WechatCleanResultGroupViewHolder;
import com.wechat.files.cleaner.item.WechatCleanResultItem;
import com.wechat.files.cleaner.utils.MemoryInfoUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.Iterator;
import java.util.List;

import static com.wechat.files.cleaner.data.bean.WechatFileCategory.CATE_CACHE;
import static com.wechat.files.cleaner.data.bean.WechatFileCategory.CATE_FILE;
import static com.wechat.files.cleaner.data.bean.WechatFileCategory.CATE_JUNK;
import static com.wechat.files.cleaner.data.bean.WechatFileCategory.CATE_VOICE;


public class WechatCleanDetailAdapter extends
        CheckableChildRecyclerViewAdapter<WechatCleanResultGroupViewHolder, WechatCleanResultChildViewHolder> {
    private static final String TAG = WechatCleanDetailAdapter.class.getSimpleName();
    private int categoryType = -2;
    private Callback mCallback;

    public WechatCleanDetailAdapter(List<WechatCleanResultItem> groups, int category) {
        super(groups);
        this.categoryType = category;
        setOnGroupExpandCollapseListener(new GroupExpandCollapseListener() {
            @Override
            public void onGroupExpanded(ExpandableGroup group) {
                ((WechatCleanResultItem) group).setExpanded(true);
            }

            @Override
            public void onGroupCollapsed(ExpandableGroup group) {
                ((WechatCleanResultItem) group).setExpanded(false);
            }
        });
    }

    public boolean isFileType() {
        return categoryType == CATE_FILE || categoryType == CATE_VOICE || categoryType == CATE_JUNK || categoryType == CATE_CACHE;
    }

    public void removeChecked() {
        int index = 0;
        Iterator<ExpandableGroup> iterator2 = (Iterator<ExpandableGroup>) expandableList.groups.iterator();
        while (iterator2.hasNext()) {
            WechatCleanResultItem typeItem = (WechatCleanResultItem) iterator2.next();
            Iterator<WechatFile> iterator = typeItem.mJunks.iterator();
            index++;
            int itemSize = 0;
            while (iterator.hasNext()) {
                WechatFile junkInfo = iterator.next();
                if (junkInfo.isChecked()) {
                    iterator.remove();
                }
                index++;
                itemSize++;
            }
            if (!typeItem.isExpanded()) {//关闭则不计算数据大小
                index = index - itemSize;
            }
            if (typeItem.mJunks.size() == 0) {
                iterator2.remove();
            }
        }
        notifyDataSetChanged();
        mCallback.onSelectItemChanged();
    }

    public void toggleChecked(boolean isChecked) {
        int size = expandableList.groups.size();
        int index = 0;
        for (int i = 0; i < size; i++) {
            WechatCleanResultItem typeItem = (WechatCleanResultItem) expandableList.groups.get(i);
            for (WechatFile junkInfo : typeItem.mJunks) {
                junkInfo.setChecked(isChecked);
                index++;
            }
            notifyItemRangeChanged(i + index - typeItem.getItemCount(), typeItem.getItemCount() + 1, new Object());
        }
        mCallback.onSelectItemChanged();
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @Override
    public WechatCleanResultChildViewHolder onCreateCheckChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())//图片格式
                .inflate(R.layout.item_wechat_clean_result_file_child, parent, false);
        if (isFileType()) {//文件格式
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_wechat_clean_result_child, parent, false);
        }
        return new WechatCleanResultChildViewHolder(view);
    }

    @Override
    public void onBindCheckChildViewHolder(final WechatCleanResultChildViewHolder holder, final int flatPosition,
                                           CheckedExpandableGroup group, final int childIndex) {
        final WechatFile info = (WechatFile) group.getItems().get(childIndex);
        holder.flWrapper.setTag(info);
        holder.flWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WechatFile file = (WechatFile) v.getTag();
                mCallback.showDetailDialog(file);
            }
        });
        if (!isFileType()) {
            adjustItemViewSize(holder, holder.flWrapper.getContext());
            info.loadIconInto(holder.mJunkIcon);
            holder.mJunkSize.setText(MemoryInfoUtil.formatShortFileSize(holder.mJunkSize.getContext(), info.getFileSize()));
        } else {
            //这里根据文件类别来设置icon
            int resourceId = -1;
            if (categoryType == CATE_VOICE) {
                resourceId = R.mipmap.ic_chat_voice;
            } else if (categoryType == CATE_FILE) {
                resourceId = R.mipmap.ic_chat_file;
            } else if (categoryType == CATE_JUNK) {
                resourceId = R.mipmap.ic_chat_junk;
            } else if (categoryType == CATE_CACHE) {
                resourceId = R.mipmap.ic_chat_cache;
            }
            Glide.with(holder.mJunkIcon.getContext())
                    .load(resourceId)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter()
                    .into(holder.mJunkIcon);
            holder.mJunkSize.setText(MemoryInfoUtil.formatShortFileSize(holder.mJunkSize.getContext(), info.getFileSize()));
        }
        holder.mJunkName.setText(info.getFilePath());
        holder.mCheckBox.setChecked(info.isChecked());
        holder.mCheckBox.setOnClickListener(v -> {
            info.setChecked(holder.mCheckBox.isChecked());
            notifyItemChanged(flatPosition - childIndex - 1, new Object());
            mCallback.onSelectItemChanged();
            EventBus.getDefault().post(new UpdateWechat());
        });
    }

    private void adjustItemViewSize(WechatCleanResultChildViewHolder holder, Context mContext) {
        WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels / 3 - 6 * dip2px(mContext, 2);
        ViewGroup.LayoutParams layoutParams = holder.flWrapper.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = width;
        holder.flWrapper.setLayoutParams(layoutParams);
    }

    private int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) ((dpValue * scale) + 0.5f);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (!isFileType()) {
            GridLayoutManager manager = (GridLayoutManager) recyclerView.getLayoutManager();
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    ExpandableListPosition listPos = expandableList.getUnflattenedPosition(position);
                    switch (listPos.type) {
                        case ExpandableListPosition.GROUP:
                            return 3;
                        case ExpandableListPosition.CHILD:
                            return 1;
                    }
                    return 3;
                }
            });
        }
    }

    @Override
    public WechatCleanResultGroupViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_wechat_scan_result_group, parent, false);
        return new WechatCleanResultGroupViewHolder(view);
    }

    @Override
    public void onBindGroupViewHolder(final WechatCleanResultGroupViewHolder holder,
                                      final int flatPosition, final ExpandableGroup group) {
        final WechatCleanResultItem typeItem = (WechatCleanResultItem) group;
        holder.mJunkTypeName.setText(typeItem.getTitle());
        String format = holder.itemView.getContext().getResources().getString(R.string.group_children_num);
        holder.mJunkItemCount.setText(String.format(format, typeItem.getItemCount()));
        holder.mTotalSize.setText(MemoryInfoUtil.formatShortFileSize(holder.itemView.getContext(),
                typeItem.getTotalSize()));
        holder.mCheckBox.setChecked(typeItem.allSelected());
        holder.mCheckBox.setOnClickListener(view -> {
            for (WechatFile junkInfo : typeItem.mJunks) {
                junkInfo.setChecked(holder.mCheckBox.isChecked());
            }
            if (typeItem.isExpanded()) {
                notifyItemRangeChanged(flatPosition + 1, typeItem.getItemCount(), new Object());
            }
            mCallback.onSelectItemChanged();
            EventBus.getDefault().post(new UpdateWechat());
        });
    }

    @Override
    public void updateChildrenCheckState(int firstChildFlattenedIndex, int numChildren) {
    }

    public interface Callback {
        void onSelectItemChanged();

        void showDetailDialog(WechatFile file);
    }
}
