package com.wechat.files.cleaner.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.text.format.Formatter;

import com.wechat.files.cleaner.R;

/**
 * Created by sean on 1/3/17.
 */

public class MemoryInfoUtil {
    public static String formatMemoryInfo(Context context, ActivityManager.MemoryInfo info) {
        return Formatter.formatFileSize(context, info.totalMem - info.availMem) + "/" +
                Formatter.formatFileSize(context, info.totalMem);
    }

    public static int getMemoryUsingProgress(ActivityManager.MemoryInfo info) {
        return (int)(100f * (info.totalMem - info.availMem) / (float)info.totalMem);
    }

    public static String formatStorageInfo(Context context, long available, long total) {
        return Formatter.formatFileSize(context, total - available) + "/" +
                Formatter.formatFileSize(context, total);
    }

    public static int getStorageUsingProgress(long available, long total) {
        return (int)(100f * (total - available) / (float)total);
    }


    public static String formatMemorySize(long size) {
        /*
        if(size <= 0) return "0B";
        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + units[digitGroups];
        */
        String[] sizeInfo = getFormattedFileSize(size);
        if(sizeInfo.length == 2) {
            return sizeInfo[0] + sizeInfo[1];
        }
        return "";
    }

    public static String[] formatMemorySizeInArray(long size) {
        /*
        if(size <= 0) return new String[] {"0", "B"};
        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
        return new String[] { new DecimalFormat("#,##0").format(size/Math.pow(1024, digitGroups)), units[digitGroups] };
        */
        return getFormattedFileSize(size);
    }

    public static double formatMemorySizeToDouble(long size) {
        if(size <= 0) return 0;
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
        return (size/Math.pow(1024, digitGroups));
    }

    public static String formatShortFileSize(Context context, long number) {
        if (context == null) {
            return "";
        }

        float result = number;
        int suffix = R.string.byte_short;
        if (result > 900) {
            suffix = R.string.kilo_byte_short;
            result = result / 1024;
        }
        if (result > 900) {
            suffix = R.string.mega_byte_short;
            result = result / 1024;
        }
        if (result > 900) {
            suffix = R.string.giga_byte_short;
            result = result / 1024F;
        }
        if (result > 900) {
            suffix = R.string.tera_byte_short;
            result = result / 1024;
        }
        if (result > 900) {
            suffix = R.string.peta_byte_short;
            result = result / 1024;
        }
        String value;
        if (result < 1) {
            value = String.format("%.2f", result);
        } else if (result < 10) {
            value = String.format("%.2f", result);
        } else if (result < 100) {
            value = String.format("%.2f", result);
        } else {
            value = String.format("%.0f", result);
        }
        return context.getResources().
                getString(R.string.clean_file_size_suffix,
                        value, context.getString(suffix));
    }

    private static String[] getFormattedFileSize(long number) {
        float result = number;
        String suffix = "B";
        if (result > 900) {
            suffix = "KB";
            result = result / 1024;
        }
        if (result > 900) {
            suffix = "MB";
            result = result / 1024;
        }
        if (result > 900) {
            suffix = "GB";
            result = result / 1024F;
        }
        if (result > 900) {
            suffix = "TB";
            result = result / 1024;
        }
        if (result > 900) {
            suffix = "PB";
            result = result / 1024;
        }
        String value;
        if (result < 1) {
            value = String.format("%.2f", result);
        } else if (result < 10) {
            value = String.format("%.2f", result);
        } else if (result < 100) {
            value = String.format("%.2f", result);
        } else {
            value = String.format("%.0f", result);
        }
        return new String[] {value, suffix};
    }
}
