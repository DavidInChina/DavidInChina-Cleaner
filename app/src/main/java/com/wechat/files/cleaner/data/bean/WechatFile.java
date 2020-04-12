package com.wechat.files.cleaner.data.bean;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.wechat.files.cleaner.R;

import java.io.File;
import java.util.HashMap;

/**
 * author:davidinchina on 2019/5/8 11:33
 * email:davicdinchina@gmail.com
 * version:1.0.0
 * des:
 */
public class WechatFile implements Parcelable {
    private String filePath;
    private long fileSize;
    private long modifyTime;
    boolean isChecked;
    private String mimeType;
    private String ext = "";

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    protected WechatFile(Parcel in) {
        filePath = in.readString();
        fileSize = in.readLong();
        modifyTime = in.readLong();
        isChecked = in.readByte() != 0;
        mimeType = in.readString();
        ext = in.readString();
    }

    public void preview(Context context) {
        String mime = FILE_TYPE.getExtToMimeTypeMap().get(ext);
        mimeType = TextUtils.isEmpty(mime) ? "*/*" : mime;
        Uri uri = Uri.fromFile(new File(filePath));
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setDataAndType(uri, mimeType);
        try {
            context.startActivity(sendIntent);
        } catch (Exception ignored) {
        }
    }

    public static final Creator<WechatFile> CREATOR = new Creator<WechatFile>() {
        @Override
        public WechatFile createFromParcel(Parcel in) {
            return new WechatFile(in);
        }

        @Override
        public WechatFile[] newArray(int size) {
            return new WechatFile[size];
        }
    };

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getFilePath() {
        return filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public long getModifyTime() {
        return modifyTime;
    }

    public WechatFile(String filePath, long fileSize, long modifyTime, String ext) {
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.modifyTime = modifyTime;
        this.ext = ext;
    }
    public WechatFile(String filePath, long fileSize, long modifyTime, String ext,boolean isChecked) {
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.modifyTime = modifyTime;
        this.ext = ext;
        this.isChecked = isChecked;
    }
    public void loadIconInto(ImageView targetView) {
        if (targetView != null && filePath != null && targetView.getContext() != null) {
            Glide.with(targetView.getContext())
                    .load(new File(filePath))
//                    .skipMemoryCache(false)
                    .placeholder(R.mipmap.ic_img_place_holder)
                    .priority(Priority.HIGH)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(targetView);
        }
    }

    private int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) ((dpValue * scale) + 0.5f);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(filePath);
        dest.writeLong(fileSize);
        dest.writeLong(modifyTime);
        dest.writeByte((byte) (isChecked ? 1 : 0));
        dest.writeString(mimeType);
        dest.writeString(ext);
    }

    final static class FILE_TYPE {
        public static HashMap<String, String> extToMimeTypeMap = new HashMap<>();

        public static HashMap<String, String> getExtToMimeTypeMap() {
            if (extToMimeTypeMap.size() <= 0) {
                initHashMap();
            }
            return extToMimeTypeMap;
        }

        public static void initHashMap() {
            extToMimeTypeMap.put("3gp", "video/3gpp");
            extToMimeTypeMap.put("asf", "video/x-ms-asf");
            extToMimeTypeMap.put("avi", "video/x-msvideo");
            extToMimeTypeMap.put("bmp", "image/bmp");
            extToMimeTypeMap.put("conf", "text/plain");
            extToMimeTypeMap.put("doc", "application/msword");
            extToMimeTypeMap.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            extToMimeTypeMap.put("xls", "application/vnd.ms-excel");
            extToMimeTypeMap.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            extToMimeTypeMap.put("gif", "image/gif");
            extToMimeTypeMap.put("htm", "text/html");
            extToMimeTypeMap.put("html", "text/html");
            extToMimeTypeMap.put("jpeg", "image/jpeg");
            extToMimeTypeMap.put("jpg", "image/jpeg");
            extToMimeTypeMap.put("log", "text/plain");
            extToMimeTypeMap.put("m3u", "audio/x-mpegurl");
            extToMimeTypeMap.put("m4a", "audio/mp4a-latm");
            extToMimeTypeMap.put("m4b", "audio/mp4a-latm");
            extToMimeTypeMap.put("m4p", "audio/mp4a-latm");
            extToMimeTypeMap.put("m4u", "video/vnd.mpegurl");
            extToMimeTypeMap.put("m4v", "video/x-m4v");
            extToMimeTypeMap.put("mov", "video/quicktime");
            extToMimeTypeMap.put("mp2", "audio/x-mpeg");
            extToMimeTypeMap.put("mp3", "audio/x-mpeg");
            extToMimeTypeMap.put("mp4", "video/mp4");
            extToMimeTypeMap.put("mpe", "video/mpeg");
            extToMimeTypeMap.put("mpeg", "video/mpeg");
            extToMimeTypeMap.put("mpg", "video/mpeg");
            extToMimeTypeMap.put("mpg4", "video/mp4");
            extToMimeTypeMap.put("mpga", "audio/mpeg");
            extToMimeTypeMap.put("ogg", "audio/ogg");
            extToMimeTypeMap.put("pdf", "application/pdf");
            extToMimeTypeMap.put("png", "image/png");
            extToMimeTypeMap.put("pps", "application/vnd.ms-powerpoint");
            extToMimeTypeMap.put("ppt", "application/vnd.ms-powerpoint");
            extToMimeTypeMap.put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
            extToMimeTypeMap.put("prop", "text/plain");
            extToMimeTypeMap.put("rc", "text/plain");
            extToMimeTypeMap.put("rmvb", "audio/x-pn-realaudio");
            extToMimeTypeMap.put("rtf", "application/rtf");
            extToMimeTypeMap.put("tar", "application/x-tar");
            extToMimeTypeMap.put("tgz", "application/x-compressed");
            extToMimeTypeMap.put("txt", "text/plain");
            extToMimeTypeMap.put("wav", "audio/x-wav");
            extToMimeTypeMap.put("wma", "audio/x-ms-wma");
            extToMimeTypeMap.put("wmv", "audio/x-ms-wmv");
            extToMimeTypeMap.put("wps", "application/vnd.ms-works");
            extToMimeTypeMap.put("xml", "text/plain");
            extToMimeTypeMap.put("zip", "application/x-zip-compressed");
        }
    }
}
