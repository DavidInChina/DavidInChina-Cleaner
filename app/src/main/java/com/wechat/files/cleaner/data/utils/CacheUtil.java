package com.wechat.files.cleaner.data.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Environment;

import com.wechat.files.cleaner.BuildConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;

/**
 * author:davidinchina on 2019/5/9 12:59
 * email:davicdinchina@gmail.com
 * version:1.0.0
 * des:
 */
public class CacheUtil {
    /**
     * 获取缓存大小
     *
     * @param context
     * @return
     * @throws Exception
     */
    public static String getTotalCacheSize(Context context) throws Exception {
        long cacheSize = 0;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File exCacheFile = context.getExternalCacheDir();
            File exFilesFolder = context.getExternalFilesDir(null);
            String path = exCacheFile.getAbsolutePath().replace(BuildConfig.APPLICATION_ID, "com.tencent.mm");
//            cacheSize += getFolderSize(new File(path));
            String exPath = exFilesFolder.getAbsolutePath().replace(BuildConfig.APPLICATION_ID, "com.tencent.mm");
            cacheSize += getFolderSize(new File(exPath));
        }
        return getFormatSize(cacheSize);
    }

    /***
     * 清理所有缓存
     * @param context
     */
    public static void clearAllCache(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteDir(context.getExternalCacheDir());
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    // 获取文件
    //Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
    //Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
    public static long getFolderSize(File file) {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    public static String getFilePath(Context context, String str) {
        File file = new File(context.getApplicationContext().getExternalFilesDir(null), "specificDBFiles");
        if (!file.exists()) {
            copyFilsFromAssert(context);
        }
        File file2 = new File(file, str);
        if (file2.exists()) {
            return file2.getAbsolutePath();
        }
        copyFilsFromAssert(context);
        return "";
    }

    private static void copyFilsFromAssert(final Context context) {
        File externalFilesDir = context.getExternalFilesDir(null);
        if (externalFilesDir != null) {
            AssetManager assets = context.getAssets();
            try {
                String[] list = assets.list("specificDBFiles");
                if (list != null) {
                    File file = new File(externalFilesDir, "specificDBFiles");
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    for (String str : list) {
                        final File file2 = new File(file, str);
                        if (!file2.exists()) {
                            final InputStream open = assets.open("specificDBFiles/" + str);
                            new AsyncTask<Void, Void, Void>() {
                                @Override
                                protected Void doInBackground(Void... voids) {
                                    FileOutputStream fileOutputStream;
                                    try {
                                        fileOutputStream = new FileOutputStream(file2);
                                        byte[] bArr = new byte[1024];
                                        while (true) {
                                            int read = open.read(bArr);
                                            if (read <= 0) {
                                                break;
                                            }
                                            fileOutputStream.write(bArr, 0, read);
                                        }
                                        if (open != null) {
                                            open.close();
                                        }
                                        fileOutputStream.close();
                                    } catch (Exception e2) {
                                        e2.printStackTrace();
                                    }
                                    return null;
                                }
                            }.execute(new Void[0]);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 格式化单位
     *
     * @param size
     * @return
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
//            return size + "Byte";
            return "0K";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }
}

