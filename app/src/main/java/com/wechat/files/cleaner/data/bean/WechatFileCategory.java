package com.wechat.files.cleaner.data.bean;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;

import com.wechat.files.cleaner.WeChatCleanDetailActivity;
import com.wechat.files.cleaner.data.ThreadPool;
import com.wechat.files.cleaner.data.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * author:davidinchina on 2019/5/8 11:36
 * email:davicdinchina@gmail.com
 * version:1.0.0
 * des:
 */
public class WechatFileCategory {
    public static final int CATE_JUNK = 0;
    public static final int CATE_CACHE = 1;
    public static final int CATE_IMG = 2;
    public static final int CATE_VIDEO = 3;
    public static final int CATE_VOICE = 4;
    public static final int CATE_FILE = 5;
    public static final int CATE_EMOJI = 6;
    public static final String CATEGORY_NAME = "CATEGORY_NAME";
    public static final String CATEGORY_TYPE = "CATEGORY_TYPE";
    private ArrayList outFiles = new ArrayList();//传入列表文件类别筛选
    private ArrayList innerFiles = new ArrayList();//遍历WeiXin文件夹下目标文件列表
    boolean isDataClear;
    boolean isDataDeleted;
    private String categoryName;
    private int iconId;
    private int categoryType;
    private long filesSize = -1;
    public static final int DATA_JUNK = 0;//可直接清理垃圾
    public static final int DATA_NORMAL = 1;//普通数据
    public int dataType = DATA_NORMAL;


    private AsyncTask task;

    public final void goCategoryDetail(Context context) {
        Intent intent = WeChatCleanDetailActivity.getWechatCleanDetail(context);
        intent.putExtra(CATEGORY_NAME, this.categoryName);
        intent.putExtra(CATEGORY_TYPE, getCategoryTypeName());
        context.startActivity(intent);
    }

    public long getFilesSize() {
        filesSize = FileUtils.scannerFilesSize(getAllFiles());
        return filesSize;
    }

    public long getSelectFilesSize() {
        long selectedSize = 0;
        for (WechatFile file : getAllFiles()
        ) {
            if (null != file && file.isChecked()) {
                selectedSize += file.getFileSize();
            }
        }
        return selectedSize;
    }

    public long getFilesSizeFirst() {
        return FileUtils.scannerFilesSize(getAllFiles());
    }

    public interface getFilesListener {
        void getFilesFinish(boolean excute);

        void getFileOneSize(long size);
    }

    public int getIconId() {
        return iconId;
    }

    public WechatFileCategory(int categoryType, String categoryName, int iconId) {
        this.categoryName = categoryName;
        this.iconId = iconId;
        this.categoryType = categoryType;
    }

    public final boolean isAutoDelete() {
        return this.categoryType == CATE_CACHE || this.categoryType == CATE_JUNK;
    }

    @SuppressLint("StaticFieldLeak")
    public void getAutoDeleteFileList(final List<String> list, final getFilesListener listener) {
        if (!this.isDataClear && null != list && list.size() > 0) {
            AsyncTask asyncTask = new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] objects) {
                    for (String str : list) {
                        if (null != str) {
                            if (str.contains("********")) {
                                List k = k();
                                if (k != null && k.size() > 0) {
                                    for (Object str2 : mergePaths(str, k)) {
                                        getFolderFiles((String) str2, listener);
                                    }
                                }
                            } else if (str.contains("/*.")) {
                                readSubFiles(str, listener);
                            } else {
                                getFolderFiles(str, listener);
                            }
                        }
                    }
                    return null;
                }

                @Override
                protected void onCancelled() {
                    super.onCancelled();
                    if (isDataClear) {
                        if (!outFiles.isEmpty()) {
                            FileUtils.sortBySize(outFiles);//排序
                        }
                        if (!innerFiles.isEmpty()) {
                            FileUtils.sortBySize(innerFiles);//排序
                        }
                        isDataClear = false;
                        if (listener != null) {
                            listener.getFilesFinish(false);//返回结果
                        }
                    }
                }

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    isDataClear = true;//清空数据
                    outFiles.clear();
                    innerFiles.clear();
                }

                @Override
                protected void onPostExecute(Object o) {
                    super.onPostExecute(o);
                    if (!outFiles.isEmpty()) {
                        FileUtils.sortBySize(outFiles);
                    }
                    if (!innerFiles.isEmpty()) {
                        FileUtils.sortBySize(innerFiles);
                    }
                    isDataClear = false;
                    if (listener != null) {
                        listener.getFilesFinish(true);//返回结果
                    }
                }
            };
            asyncTask.executeOnExecutor(ThreadPool.getInstance().threadPoolExecutor, new Void[0]);
        }
    }

    private void readSubFiles(String str, final getFilesListener listener) {
        File file = new File(Environment.getExternalStorageDirectory(), str);
        File parentFile = file.getParentFile();
        String substring = file.getName().substring(1);
        if (parentFile.exists() && parentFile.isDirectory()) {
            for (File file2 : parentFile.listFiles()) {
                if (file2.getAbsolutePath().toLowerCase().endsWith(substring.toLowerCase())) {
                    //添加文件
                    long length = (file2.exists() && file2.isFile()) ? file2.length() : 0;
                    outFiles.add(new WechatFile(file2.getAbsolutePath(), length, new Date(file2.lastModified()).getTime(), FileUtils.getFileExt(file.getAbsolutePath()), true));
                    listener.getFileOneSize(length);
                }
            }
        }
    }

    private void getFolderFiles(String str, final getFilesListener listener) {
        File file = new File(Environment.getExternalStorageDirectory(), str);
        if (file.exists()) {
            readInnerFiles(file, listener);
        }
    }

    private void readInnerFiles(File file, final getFilesListener listener) {
        File[] listFiles = file.listFiles();
        if (listFiles != null && listFiles.length > 0) {
            for (File file2 : listFiles) {
                if (file2.isDirectory()) {
                    readInnerFiles(file2, listener);
                } else if (!file2.getName().equals(".nomedia")) {
                    //这里添加文件
                    long length = (file2.exists() && file2.isFile()) ? file2.length() : 0;
                    outFiles.add(new WechatFile(file2.getAbsolutePath(), length, new Date(file2.lastModified()).getTime(), FileUtils.getFileExt(file.getAbsolutePath()), true));
                    listener.getFileOneSize(length);
                }
            }
        }
    }

    private List<String> mergePaths(String str, List<String> list) {
        List<String> arrayList = new ArrayList();
        for (String replace : list) {
            arrayList.add(str.replace("********", replace));
        }
        return arrayList;
    }

    private List<String> k() {
        return FileUtils.getCompileFiles("tencent/MicroMsg/", "[A-Za-z0-9]{32}");
    }

    @SuppressLint("StaticFieldLeak")
    public final void getFilelistAsync(final List<File> list, final getFilesListener listener) {
        if (!this.isDataClear && null != list && list.size() > 0) {
            this.task = new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] objects) {
                    if (!"".equals(getFileTile())) {
                        FileUtils.scannerInnerFiles(new File(FileUtils.getWechatFilesRoot()
                                , "WeiXin"), new FileUtils.getInnerFileListener() {//深度优先遍历出对应文件列表
                            public final void innerFiles(File file, long j) {
                                if (file.getAbsolutePath().endsWith(getFileTile())) {
                                    innerFiles.add(new WechatFile(file.getAbsolutePath(), j, new Date(file.lastModified()).getTime()
                                            , FileUtils.getFileExt(file.getAbsolutePath())));
                                    listener.getFileOneSize(j);
                                }
                            }
                        }, 1, 1);
                    }
                    long size = FileUtils.scannerOutFiles(list, outFiles, getFileTile(), listener);
                    return null;
                }

                @Override
                protected void onCancelled() {
                    super.onCancelled();
                    if (isDataClear) {
                        if (!outFiles.isEmpty()) {
                            FileUtils.sortByTime(outFiles);//排序
                        }
                        if (!innerFiles.isEmpty()) {
                            FileUtils.sortByTime(innerFiles);//排序
                        }
                        isDataClear = false;
                        if (listener != null) {
                            listener.getFilesFinish(false);//返回结果
                        }
                    }
                }

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    isDataClear = true;//清空数据
                    outFiles.clear();
                    innerFiles.clear();
                }

                @Override
                protected void onPostExecute(Object o) {
                    super.onPostExecute(o);
                    if (!outFiles.isEmpty()) {
                        FileUtils.sortByTime(outFiles);
                    }
                    if (!innerFiles.isEmpty()) {
                        FileUtils.sortByTime(innerFiles);
                    }
                    isDataClear = false;
                    if (listener != null) {
                        listener.getFilesFinish(true);//返回结果
                    }
                }
            };
            this.task.executeOnExecutor(ThreadPool.getInstance().threadPoolExecutor, new Void[0]);
        } else {
            if (listener != null) {
                listener.getFilesFinish(false);//返回结果
            }
        }
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getCategoryType() {
        return categoryType;
    }

    public final String getCategoryTypeName() {
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

    public String getFileTile() {
        switch (categoryType) {
            case CATE_VIDEO:
                return ".mp4";
            case CATE_EMOJI:
                return "";
            case CATE_FILE:
                return "";
            case CATE_IMG:
                return ".jpg";
            case CATE_VOICE:
                return "";
        }
        return "";
    }

    public final List<WechatFile> getAllFiles() {
        List arrayList = new ArrayList();
        arrayList.addAll(this.outFiles);
        arrayList.addAll(this.innerFiles);
        return arrayList;
    }

    public final List<WechatFile> getOutFiles() {
        List arrayList = new ArrayList();
        arrayList.addAll(this.outFiles);
        return arrayList;
    }

    public final List<WechatFile> getInFiles() {
        List arrayList = new ArrayList();
        arrayList.addAll(this.innerFiles);
        return arrayList;
    }

    public final boolean isNoData() {
        return this.isDataClear;
    }

    public final void deleteFiles() {
        if (!this.isDataDeleted) {
            this.isDataDeleted = true;
            FileUtils.deleteFiles(this.innerFiles);
            FileUtils.deleteFiles(this.outFiles);
            this.isDataDeleted = false;
        }
    }

    public final void deleteFileSingle(WechatFile file) {
        if (!this.innerFiles.isEmpty()) {
            Iterator it = this.innerFiles.iterator();
            while (it.hasNext()) {
                WechatFile file2 = (WechatFile) it.next();
                if (file2.getFilePath().equals(file.getFilePath())) {
                    FileUtils.deleteFile(file2.getFilePath());
                    it.remove();
                }
            }
        }
        if (!this.outFiles.isEmpty()) {
            Iterator it = this.outFiles.iterator();
            while (it.hasNext()) {
                WechatFile file2 = (WechatFile) it.next();
                if (file2.getFilePath().equals(file.getFilePath())) {
                    FileUtils.deleteFile(file2.getFilePath());
                    it.remove();
                }
            }
        }
    }

    public final void toggleChecked(boolean isChecked) {
        if (!this.innerFiles.isEmpty()) {
            for (Object innerFile : this.innerFiles) {
                WechatFile file2 = (WechatFile) innerFile;
                if (null != file2) {
                    file2.setChecked(isChecked);
                }
            }
        }
        if (!this.outFiles.isEmpty()) {
            for (Object outFile : this.outFiles) {
                WechatFile file2 = (WechatFile) outFile;
                if (null != file2) {
                    file2.setChecked(isChecked);
                }
            }
        }
    }
}
