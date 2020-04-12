package com.wechat.files.cleaner.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.FragmentActivity;

import com.wechat.files.cleaner.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by webeyejoe on 17-3-14.
 */

public class PackageUtils {

    public static boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static String getAppName(String packageName, PackageManager packageManager) {
        try {
            PackageInfo info = packageManager.getPackageInfo(packageName, 0);
            return packageManager.getApplicationLabel(info.applicationInfo).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean activityNotAvailable(FragmentActivity activity) {
        return null == activity
                || activity.isFinishing()
                || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed());
    }

    public synchronized static List<PackageInfo> getInstalledAppList(Context context) {
        List<PackageInfo> installAppList = new ArrayList<>();
        try {
            PackageManager pm = context.getPackageManager();
            List<PackageInfo> appList = pm.getInstalledPackages(0);
            if (appList != null && appList.size() > 0) {
                installAppList.addAll(appList);
            }
            else {
                Intent intent = new Intent(Intent.ACTION_MAIN, null);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);

                PackageManager mPm = context.getPackageManager();
                List<ResolveInfo> allApps = mPm.queryIntentActivities(intent, 0);
                for (ResolveInfo ri : allApps) {
                    if (ri.activityInfo.packageName.equals(context.getPackageName())
                            || ri.activityInfo.packageName.equals("com.android.settings")) {
                        continue;
                    }
                    ApplicationInfo appInfo = pm.getApplicationInfo(ri.activityInfo.packageName, 0);
                    PackageInfo pi = new PackageInfo();
                    pi.applicationInfo = appInfo;
                    pi.packageName = ri.activityInfo.packageName;
                    installAppList.add(pi);
                }
            }
        } catch (Exception e) {
        }
        return installAppList;
    }
}
