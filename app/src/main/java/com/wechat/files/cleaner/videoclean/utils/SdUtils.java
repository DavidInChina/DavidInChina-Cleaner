package com.wechat.files.cleaner.videoclean.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.support.v4.provider.DocumentFile;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SdUtils {
    public static DocumentFile getDocumentFile(File file, boolean z, Context context, Uri uri) {
        int i = 0;
        String extSdCardFolder = getExtSdCardFolder(file, context);
        if (extSdCardFolder == null) {
            return null;
        }
        int i2;
        try {
            int i3;
            String str;
            String canonicalPath = file.getCanonicalPath();
            if (extSdCardFolder.equals(canonicalPath)) {
                i3 = 1;
                str = null;
            } else {
                str = canonicalPath.substring(extSdCardFolder.length() + 1);
                i3 = 0;
            }
            i2 = i3;
            extSdCardFolder = str;
        } catch (IOException e) {
            return null;
        } catch (Exception e2) {
            extSdCardFolder = null;
            i2 = 1;
        }
        if (uri == null) {
            return null;
        }
        DocumentFile fromTreeUri = DocumentFile.fromTreeUri(context, uri);
        if (i2 != 0) {
            return fromTreeUri;
        }
        String[] split = extSdCardFolder.split("\\/");
        while (i < split.length) {
            DocumentFile findFile = fromTreeUri.findFile(split[i]);
            if (findFile != null) {
                fromTreeUri = findFile;
            } else if (i >= split.length - 1 && !z) {
                fromTreeUri = fromTreeUri.createFile("image", split[i]);
            } else if (fromTreeUri.createDirectory(split[i]) == null) {
                return null;
            } else {
                fromTreeUri = fromTreeUri.createDirectory(split[i]);
            }
            i++;
        }
        return fromTreeUri;
    }

    @TargetApi(19)
    public static String getExtSdCardFolder(File file, Context context) {
        String[] extSdCardPaths = getExtSdCardPaths(context);
        int i = 0;
        while (i < extSdCardPaths.length) {
            try {
                if (file.getCanonicalPath().startsWith(extSdCardPaths[i])) {
                    return extSdCardPaths[i];
                }
                i++;
            } catch (IOException e) {
                return null;
            }
        }
        return null;
    }

    @TargetApi(19)
    public static String[] getExtSdCardPaths(Context context) {
        List arrayList = new ArrayList();
        for (File file : context.getExternalFilesDirs("external")) {
            if (!(file == null || file.equals(context.getExternalFilesDir("external")))) {
                int lastIndexOf = file.getAbsolutePath().lastIndexOf("/Android/data");
                if (lastIndexOf < 0) {
                    Log.w("AmazeFileUtils", "Unexpected external file dir: " + file.getAbsolutePath());
                } else {
                    String  substring = file.getAbsolutePath().substring(0, lastIndexOf);
                    try {
                        substring = new File(substring).getCanonicalPath();
                    } catch (IOException e) {
                    }
                    arrayList.add(substring);
                }
            }
        }
        if (arrayList.isEmpty()) {
            arrayList.add("/storage/sdcard1");
        }
        return (String[]) arrayList.toArray(new String[0]);
    }

    @TargetApi(21)
    public static boolean deleteFiles(File file, Uri uri, Context context) {
        try {
            DocumentFile documentFile = getDocumentFile(file, false, context, uri);
            if (documentFile != null) {
                documentFile.delete();
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
