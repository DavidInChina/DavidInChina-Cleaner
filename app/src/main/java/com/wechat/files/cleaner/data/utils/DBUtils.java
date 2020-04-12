package com.wechat.files.cleaner.data.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.text.TextUtils;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * author:davidinchina on 2019/5/9 20:58
 * email:davicdinchina@gmail.com
 * version:1.0.0
 * des:
 */
public class DBUtils {
    public static List<String> junks = new ArrayList();
    public static List<String> caches = new ArrayList();
    public static void readDBfils(Context context) {
        junks.clear();
        caches.clear();
        String secret = "30820309308201f1a00302010202043c3a301d300d06092a864886f70d01010b05003035310b300906035504061302383631";//
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(context.getAssets().open("wechat.db")));
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    String result = decode(secret, readLine);
                    getData(result);
                } else {
                    bufferedReader.close();
                    return;
                }
            }
        } catch (Exception e) {
        }
    }

    public static String getSignature(Context context) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pi;
        StringBuilder sb = new StringBuilder();
        try {
            pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            Signature[] signatures = pi.signatures;
            for (Signature signature : signatures) {
                sb.append(signature.toCharsString());
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private static final byte[] code = "12345678".getBytes();

    public static String decode(String str, String str2) {
        try {
            return new String(desDecode(str, 2).doFinal(Base64.decode(str2, 0)));
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return "";
    }
    private static void getData(String str) {
        String[] split = str.split(",");
        int parseInt = TextUtils.isEmpty(split[1]) ? 0 : Integer.parseInt(split[1]);
        String obj = split[0];
        switch (parseInt) {
            case 0:
                junks.add(obj);
                return;
            case 1:
                caches.add(obj);
                return;
            default:
                return;
        }
    }
    private static Cipher desDecode(String str, int i) {
        Key generateSecret = null;
        Cipher instance = null;
        try {
            generateSecret = SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(str.getBytes()));
            instance = Cipher.getInstance("DES/CBC/PKCS5Padding");
            instance.init(i, generateSecret, new IvParameterSpec(code));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return instance;
    }
}
