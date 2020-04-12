package com.wechat.files.cleaner.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Map;
import java.util.Set;

public class PrefsCleanUtil {
    public static Editor editor;
    public static SharedPreferences prefs;
    private static PrefsCleanUtil prefsUtil;
    public Context context;

    public static synchronized PrefsCleanUtil getInstance() {
        PrefsCleanUtil prefsCleanUtil;
        synchronized (PrefsCleanUtil.class) {
            prefsCleanUtil = prefsUtil;
        }
        return prefsCleanUtil;
    }

    public static void init(Context context, String str, int i) {
        prefsUtil = new PrefsCleanUtil();
        prefsUtil.context = context;
        prefs = prefsUtil.context.getSharedPreferences(str, i);
        editor = prefs.edit();
    }

    private PrefsCleanUtil() {
    }

    public boolean getBoolean(String str, boolean z) {
        if (prefs == null || this.context == null) {
            return false;
        }
        return prefs.getBoolean(str, z);
    }

    public boolean getBoolean(String str) {
        if (prefs == null || this.context == null) {
            return false;
        }
        return prefs.getBoolean(str, false);
    }

    public String getString(String str, String str2) {
        if (prefs == null || this.context == null) {
            return "";
        }
        return prefs.getString(str, str2);
    }

    public String getString(String str) {
        if (prefs == null || this.context == null) {
            return "";
        }
        return prefs.getString(str, null);
    }

    public int getInt(String str, int i) {
        if (prefs == null || this.context == null) {
            return 0;
        }
        return prefs.getInt(str, i);
    }

    public int getInt(String str) {
        if (prefs == null || this.context == null) {
            return 0;
        }
        return prefs.getInt(str, 0);
    }

    public float getFloat(String str, float f) {
        return prefs.getFloat(str, f);
    }

    public float getFloat(String str) {
        return prefs.getFloat(str, 0.0f);
    }

    public long getLong(String str, long j) {
        if (prefs == null || this.context == null) {
            return 0;
        }
        return prefs.getLong(str, j);
    }

    public long getLong(String str) {
        if (prefs == null || this.context == null) {
            return 0;
        }
        return prefs.getLong(str, 0);
    }

    @TargetApi(11)
    public Set<String> getStringSet(String str, Set<String> set) {
        return prefs.getStringSet(str, set);
    }

    @TargetApi(11)
    public Set<String> getStringSet(String str) {
        return prefs.getStringSet(str, null);
    }

    public Map<String, ?> getAll() {
        return prefs.getAll();
    }

    public void putString(String str, String str2) {
        editor.putString(str, str2);
        editor.apply();
    }

    public void putInt(String str, int i) {
        editor.putInt(str, i);
        editor.apply();
    }

    public void putFloat(String str, float f) {
        editor.putFloat(str, f);
        editor.apply();
    }

    public void putLong(String str, long j) {
        editor.putLong(str, j);
        editor.apply();
    }

    public void putBoolean(String str, boolean z) {
        editor.putBoolean(str, z);
        editor.apply();
    }

    public void commit() {
        editor.commit();
    }

    @TargetApi(11)
    public PrefsCleanUtil putStringSet(String str, Set<String> set) {
        editor.putStringSet(str, set);
        editor.commit();
        return this;
    }

    public void removeKey(String str) {
        editor.remove(str);
        editor.apply();
    }
}
