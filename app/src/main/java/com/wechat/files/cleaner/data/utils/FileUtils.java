package com.wechat.files.cleaner.data.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.wechat.files.cleaner.data.bean.WechatFile;
import com.wechat.files.cleaner.data.bean.WechatFileCategory;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * author:davidinchina on 2019/5/8 14:57
 * email:davicdinchina@gmail.com
 * version:1.0.0
 * des:
 */
public class FileUtils {
    public static boolean deleteFile(String str) {
        try {
            File file = new File(str);
            return file.exists() && file.delete();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void deleteFiles(List<WechatFile> list) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            WechatFile file = (WechatFile) it.next();
            if (file.isChecked()) {
                deleteFile(file.getFilePath());
                it.remove();
            }
        }
    }
    public static void removeSelectFiles(List<WechatFile> list) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            WechatFile file = (WechatFile) it.next();
            if (file.isChecked()) {
//                deleteFile(file.getFilePath());
                it.remove();
            }
        }
    }
    public interface getInnerFileListener {
        void innerFiles(File file, long j);
    }

    public static File getWechatFilesRoot() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/tencent/MicroMsg");
        return !file.exists() ? new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Tencent/MicroMsg") : file;
    }

    public static void scannerInnerFiles(File file, getInnerFileListener aVar, int i, int i2) {
        if (file != null && i <= i2) {
            File[] listFiles = file.listFiles();
            if (listFiles != null) {
                for (File file2 : listFiles) {
                    if (file2.isDirectory()) {
                        scannerInnerFiles(file2, aVar, i + 1, i2);
                    } else if (!TextUtils.equals(file2.getName(), ".nomedia")) {
                        long length = (file2.exists() && file2.isFile()) ? file2.length() : 0;
                        if (length != 0) {
                            aVar.innerFiles(file2, length);
                        }
                    }
                }
            }
        }
    }

    static long size = 0;

    public static long scannerOutFiles(List<File> list, final List<WechatFile> list2, final String str, WechatFileCategory.getFilesListener listener) {
        size = 0;
        for (File o : list) {
            scannerInnerFiles(o, new getInnerFileListener() {
                public final void innerFiles(File file, long length) {
                    if (TextUtils.isEmpty(str) || file.getAbsolutePath().endsWith(str)) {
                        list2.add(new WechatFile(file.getAbsolutePath(), length, new Date(file.lastModified()).getTime()
                                , FileUtils.getFileExt(file.getAbsolutePath())));
                        listener.getFileOneSize(length);
                        size += length;
                    }
                }
            }, 1, 3);
        }
        return size;
    }

    public static String getFileExt(String uriString) {
        int extIndex = uriString.lastIndexOf(".");
        if (uriString == null || extIndex == -1) return "";
        else return uriString.substring(extIndex + 1);
    }

    public static long scannerFilesSize(final List<WechatFile> list2) {
        long size2 = 0;
        if (null != list2 && list2.size() > 0) {
            for (WechatFile file : list2) {
                if (null != file) {
                    size2 += file.getFileSize();
                }
            }
        }
        return size2;
    }

    public static long scannerFilesCheckedSize(final List<WechatFile> list2) {
        long size2 = 0;
        try {
            if (null != list2 && list2.size() > 0) {
                for (WechatFile file : list2) {
                    if (file.isChecked()) {
                        size2 += file.getFileSize();
                    }
                }
            }
        } catch (Exception ignored) {

        }
        return size2;
    }

    public static void sortBySize(List<WechatFile> list) {
        Collections.sort(list, new Comparator<WechatFile>() {
            public final /* bridge */ /* synthetic */ int compare(WechatFile obj, WechatFile obj2) {
                long j = obj2.getFileSize() - obj.getFileSize();
                return j > 0 ? 1 : j < 0 ? -1 : 0;
            }
        });
    }

    public static void sortByTime(List<WechatFile> list) {
        Collections.sort(list, new Comparator<WechatFile>() {
            public final /* bridge */ /* synthetic */ int compare(WechatFile obj, WechatFile obj2) {
                long time1 = obj.getModifyTime();
                long time2 = obj2.getModifyTime();
                long j = time2 - time1;
                return j > 0 ? 1 : j < 0 ? -1 : 0;
            }
        });
    }

    public static boolean isTimeRefresh(Context context) {
        return System.currentTimeMillis() - (Long) SpUtils.get(context, "PREF_KEY_LAST_SCAN_TIME", 0L) >= 120000;
    }

    public static List<String> getCompileFiles(String str, String str2) {
        List<String> arrayList = new ArrayList();
        File file = new File(Environment.getExternalStorageDirectory(), str);
        Pattern compile = Pattern.compile(str2);
        if (file.exists() && file.isDirectory()) {
            for (File file2 : file.listFiles()) {
                if (compile.matcher(file2.getName()).matches()) {
                    arrayList.add(file2.getName());
                }
            }
        }
        return arrayList;
    }
}
