package com.wechat.files.cleaner.data;

import android.content.Context;

import com.wechat.files.cleaner.R;
import com.wechat.files.cleaner.data.bean.WechatFile;
import com.wechat.files.cleaner.data.bean.WechatFileCategory;
import com.wechat.files.cleaner.data.utils.DBUtils;
import com.wechat.files.cleaner.data.utils.FileUtils;
import com.wechat.files.cleaner.data.utils.SpUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wechat.files.cleaner.data.bean.WechatFileCategory.CATE_CACHE;
import static com.wechat.files.cleaner.data.bean.WechatFileCategory.CATE_EMOJI;
import static com.wechat.files.cleaner.data.bean.WechatFileCategory.CATE_FILE;
import static com.wechat.files.cleaner.data.bean.WechatFileCategory.CATE_IMG;
import static com.wechat.files.cleaner.data.bean.WechatFileCategory.CATE_JUNK;
import static com.wechat.files.cleaner.data.bean.WechatFileCategory.CATE_VIDEO;
import static com.wechat.files.cleaner.data.bean.WechatFileCategory.CATE_VOICE;

/**
 * author:davidinchina on 2019/5/8 11:22
 * email:davicdinchina@gmail.com
 * version:1.0.0
 * des:
 */
public class WechatDataManager {
    public static final String CATEGORY_VIDEO = "video";
    public static final String CATEGORY_IMAGE = "image2";
    public static final String CATEGORY_VOICE = "voice2";
    public static final String CATEGORY_FILES = "Download";
    public static final String CATEGORY_EMOJI = "emoji";
    public static final String CATEGORY_JUNK = "junk";
    public static final String CATEGORY_CACHE = "cache";
    private static WechatDataManager instance;
    private Context context;
    private Map categories = new HashMap();
    private Map hashMap = new HashMap();
    private long categoryTotalSize = -1L;
    private long categorySelectedlSize = -1L;
    private boolean isScanning;

    private WechatDataManager(Context context) {
        this.context = context;
    }

    public interface InitDataListener {
        void initDataFinish();
    }

    public interface WechatCategoryListener {
        void getWechatCategory(WechatFileCategory category);

        void onWechatInfoSizeUpdate(long size);

        void onWechatInfoSizeSucceed(long size);
    }

    public void initData(final InitDataListener listener) {
        if (hashMap.isEmpty() || FileUtils.isTimeRefresh(context)) {
            initFileCategory();
            ThreadPool.getInstance().threadPoolExecutor.execute(() -> {
                if (!isScanning) {
                    isScanning = true;
                    initFileList();
                    isScanning = false;
                    if (null != listener) {
                        listener.initDataFinish();
                        SpUtils.put(context, "PREF_KEY_LAST_SCAN_TIME", System.currentTimeMillis());
                    }
                }
            });
        } else {
            listener.initDataFinish();
        }
    }

    public WechatFileCategory getWechatCategory(String key) {
        return (WechatFileCategory) categories.get(key);
    }

    public void deleteWechatFiles(List<WechatFile> mFiles, int categoryType) {
        for (WechatFile f :
                mFiles) {
            if (f.isChecked()) {
                getWechatCategory(getCategoryTypeName(categoryType)).deleteFileSingle(f);
            }
        }
    }

    public void deleteAllSelectedWechatFiles() {
        if (!categories.isEmpty()) {
            for (Object key : categories.keySet()
            ) {
                WechatFileCategory category = (WechatFileCategory) categories.get(key);
                if (null != category) {
                    deleteWechatFiles(category.getAllFiles(), category.getCategoryType());
                }
            }
        }
    }

    public final String getCategoryTypeName(int categoryType) {
        switch (categoryType) {
            case CATE_VIDEO:
                return "video";
            case CATE_EMOJI:
                return "emoji";
            case CATE_FILE:
                return "Download";
            case CATE_IMG:
                return "image2";
            case CATE_VOICE:
                return "voice2";
            case CATE_JUNK:
                return "junk";
            case CATE_CACHE:
                return "cache";
        }
        return "video";
    }

    public void getWechatCategory(final WechatCategoryListener listener) {
        categoryTotalSize = -1L;
        categorySelectedlSize = -1L;
        initData(() -> {
            for (Object key : categories.keySet()
            ) {
                getWechatCategory((String) key, listener);
            }
        });
    }

    public void getWechatCategory(String key, final WechatCategoryListener listener) {
        final WechatFileCategory category = (WechatFileCategory) categories.get(key);
        if (category.isAutoDelete()) {
            category.getAutoDeleteFileList((List<String>) hashMap.get(key), new WechatFileCategory.getFilesListener() {
                @Override
                public void getFilesFinish(boolean excute) {
                    listener.getWechatCategory(category);
                    listener.onWechatInfoSizeSucceed(getCategoryTotalSize());
                }

                @Override
                public void getFileOneSize(long size) {
                    listener.onWechatInfoSizeUpdate(size);
                }
            });
        } else {
            category.getFilelistAsync((List<File>) hashMap.get(key), new WechatFileCategory.getFilesListener() {
                @Override
                public void getFilesFinish(boolean excute) {
                    listener.getWechatCategory(category);
                    listener.onWechatInfoSizeSucceed(getCategoryTotalSize());
                }

                @Override
                public void getFileOneSize(long size) {
                    listener.onWechatInfoSizeUpdate(size);
                }
            });
        }
    }

    public static WechatDataManager getInstance(Context context) {
        if (null == instance) {
            synchronized (WechatDataManager.class) {
                if (null == instance) {
                    instance = new WechatDataManager(context);
                }
            }
        }
        return instance;
    }

    private void initFileCategory() {
        categories.put(CATEGORY_EMOJI, new WechatFileCategory(CATE_EMOJI, context.getString(R.string.str_wechat_emoji), R.mipmap.ic_chat_emoji));
        categories.put(CATEGORY_VIDEO, new WechatFileCategory(CATE_VIDEO, context.getString(R.string.str_wechat_video), R.mipmap.ic_chat_video));
        categories.put(CATEGORY_IMAGE, new WechatFileCategory(CATE_IMG, context.getString(R.string.str_wechat_save_img), R.mipmap.ic_chat_img));
        categories.put(CATEGORY_FILES, new WechatFileCategory(CATE_FILE, context.getString(R.string.str_wechat_file), R.mipmap.ic_chat_file));
        categories.put(CATEGORY_VOICE, new WechatFileCategory(CATE_VOICE, context.getString(R.string.str_wechat_voice), R.mipmap.ic_chat_voice));
        categories.put(CATEGORY_JUNK, new WechatFileCategory(CATE_JUNK, context.getString(R.string.str_wechat_junks), R.mipmap.ic_chat_junk));
        categories.put(CATEGORY_CACHE, new WechatFileCategory(CATE_CACHE, context.getString(R.string.str_wechat_cache), R.mipmap.ic_chat_cache));
    }

    public long getCategoryTotalSize() {
        initSizeCount();
        return categoryTotalSize;
    }

    public long getCategoryJunkSize() {
        return initJunkSizeCount();
    }

    public void initSizeCount() {
        categoryTotalSize = 0;
        if (!categories.isEmpty()) {
            for (Object key : categories.keySet()
            ) {
                WechatFileCategory category = (WechatFileCategory) categories.get(key);
                if (null != category) {
                    categoryTotalSize += category.getFilesSizeFirst();
                }
            }
        }
    }

    public long initJunkSizeCount() {
        long size = 0;
        if (!categories.isEmpty()) {
            for (Object key : categories.keySet()
            ) {
                WechatFileCategory category = (WechatFileCategory) categories.get(key);
                if (null != category && (category.getCategoryType() == CATE_CACHE || category.getCategoryType() == CATE_JUNK)) {
                    size += category.getFilesSizeFirst();
                }
            }
        }
        return size;
    }

    public long getCategorySelectedSize() {
        initSelectedSizeCount();
        return categorySelectedlSize;
    }

    public void initSelectedSizeCount() {
        categorySelectedlSize = 0;
        if (!categories.isEmpty()) {
            for (Object key : categories.keySet()
            ) {
                WechatFileCategory category = (WechatFileCategory) categories.get(key);
                if (null != category) {
                    for (WechatFile file : category.getAllFiles()
                    ) {
                        if (null != file && file.isChecked()) {
                            categorySelectedlSize += file.getFileSize();
                        }
                    }
                }
            }
        }
    }

    private void initFileList() {
        File root = FileUtils.getWechatFilesRoot();
        File[] listFiles = root.listFiles();
        if (listFiles == null || listFiles.length == 0) {
            //回调没有数据
            return;
        }
        //添加各个分类对应的文件夹路径
        hashMap.put(CATEGORY_IMAGE, new ArrayList());
        hashMap.put(CATEGORY_EMOJI, new ArrayList());
        hashMap.put(CATEGORY_FILES, new ArrayList());
        hashMap.put(CATEGORY_VIDEO, new ArrayList());
        hashMap.put(CATEGORY_VOICE, new ArrayList());
        hashMap.put(CATEGORY_JUNK, new ArrayList());
        hashMap.put(CATEGORY_CACHE, new ArrayList());
        for (File file : listFiles) {
            if (file.isDirectory()) {
                File[] listFiles2 = file.listFiles();
                if (listFiles2 != null) {
                    for (File file2 : listFiles2) {
                        String name = file2.getName();
                        if (name.equals(CATEGORY_IMAGE) ||
                                name.equals(CATEGORY_VOICE) ||
                                name.equals(CATEGORY_EMOJI) ||
                                name.equals(CATEGORY_VIDEO)
                        ) {
                            ((ArrayList) hashMap.get(name)).add(file2);
                        }
                    }
                }
            }
        }
        ((ArrayList) hashMap.get(CATEGORY_FILES)).add(new File(root, CATEGORY_FILES));
        DBUtils.readDBfils(context);//这里就逆向得到的缓存路径集合
        ((ArrayList) hashMap.get(CATEGORY_JUNK)).addAll(DBUtils.junks);
        ((ArrayList) hashMap.get(CATEGORY_CACHE)).addAll(DBUtils.caches);
    }

}
