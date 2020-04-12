package com.wechat.files.cleaner.videoclean;

import android.content.ContentUris;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore.Audio.Media;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.wechat.files.cleaner.R;
import com.wechat.files.cleaner.WechatCleannerApp;
import com.wechat.files.cleaner.utils.PrefsCleanUtil;
import com.wechat.files.cleaner.videoclean.bean.CleanShortVideoInfo;
import com.wechat.files.cleaner.videoclean.bean.CleanVideoforEvenBusInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MusicLoader {
    public static final String CLEAN_VIDEO_TOTAL_SIZE = "clean_video_total_size";
    private Uri contentUri;
    private DisplayMetrics metrics;
    private long startScanShortVideoTime;

    private MusicLoaderLinstener mMusicLoaderLinstener;
    public interface MusicLoaderLinstener {
        void onLoadVideoInfo(CleanVideoforEvenBusInfo cleanVideoforEvenBusInfo);
    }
    public static class MusicInfo implements Parcelable {
        public static final Creator<MusicInfo> CREATOR = new Creator<MusicInfo>() {
            public MusicInfo[] newArray(int i) {
                return new MusicInfo[i];
            }

            public MusicInfo createFromParcel(Parcel parcel) {
                MusicInfo musicInfo = new MusicInfo();
                musicInfo.setId(parcel.readLong());
                musicInfo.setTitle(parcel.readString());
                musicInfo.setAlbum(parcel.readString());
                musicInfo.setArtist(parcel.readString());
                musicInfo.setUrl(parcel.readString());
                musicInfo.setDuration(parcel.readInt());
                musicInfo.setSize(parcel.readLong());
                return musicInfo;
            }
        };
        private String album;
        private String artist;
        private String buildDate;
        private int duration;
        private String fromSoure;
        private int fromType;
        private long id;
        private String imgUrl;
        private boolean isChecked;
        private long size;
        private String thumbPath;
        private Drawable thumpIcon;
        private String title;
        private long updateTime;
        private String url;
        private String videoAbsolutelyPath;

        public String getVideoAbsolutelyPath() {
            return this.videoAbsolutelyPath;
        }

        public void setVideoAbsolutelyPath(String str) {
            this.videoAbsolutelyPath = str;
        }

        public int getFromType() {
            return this.fromType;
        }

        public void setFromType(int i) {
            this.fromType = i;
        }

        public Drawable getThumpIcon() {
            return this.thumpIcon;
        }

        public void setThumpIcon(Drawable drawable) {
            this.thumpIcon = drawable;
        }

        public String getFromSoure() {
            return this.fromSoure;
        }

        public void setFromSoure(String str) {
            this.fromSoure = str;
        }

        public String getBuildDate() {
            return this.buildDate;
        }

        public void setBuildDate(String str) {
            this.buildDate = str;
        }

        public String getImgUrl() {
            return this.imgUrl;
        }

        public void setImgUrl(String str) {
            this.imgUrl = str;
        }

        public String getThumbPath() {
            return this.thumbPath;
        }

        public void setThumbPath(String str) {
            this.thumbPath = str;
        }

        public boolean isChecked() {
            return this.isChecked;
        }

        public void setChecked(boolean z) {
            this.isChecked = z;
        }

        public long getUpdateTime() {
            return this.updateTime;
        }

        public void setUpdateTime(long j) {
            this.updateTime = j;
        }

        public MusicInfo(long j, String str) {
            this.id = j;
            this.title = str;
        }

        public MusicInfo() {
        }

        public String getArtist() {
            return this.artist;
        }

        public void setArtist(String str) {
            this.artist = str;
        }

        public long getSize() {
            return this.size;
        }

        public void setSize(long j) {
            this.size = j;
        }

        public long getId() {
            return this.id;
        }

        public void setId(long j) {
            this.id = j;
        }

        public String getTitle() {
            return this.title;
        }

        public void setTitle(String str) {
            this.title = str;
        }

        public String getAlbum() {
            return this.album;
        }

        public void setAlbum(String str) {
            this.album = str;
        }

        public int getDuration() {
            return this.duration;
        }

        public void setDuration(int i) {
            this.duration = i;
        }

        public String getUrl() {
            return this.url;
        }

        public void setUrl(String str) {
            this.url = str;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeLong(this.id);
            parcel.writeString(this.title);
            parcel.writeString(this.album);
            parcel.writeString(this.artist);
            parcel.writeString(this.url);
            parcel.writeInt(this.duration);
            parcel.writeLong(this.size);
        }
    }

    public MusicLoader() {
        this.contentUri = Media.EXTERNAL_CONTENT_URI;
    }
    public void setMusicLoaderLinstener(MusicLoaderLinstener musicLoaderLinstener) {
        this.mMusicLoaderLinstener = musicLoaderLinstener;
    }
    public List<CleanShortVideoInfo> getShortVideoList() {
        long j = 0;
        this.startScanShortVideoTime = System.currentTimeMillis();
        List<CleanShortVideoInfo> arrayList = new ArrayList(hasDirecFileVideo());
        if (arrayList.size() <= 0) {
            PrefsCleanUtil.getInstance().putLong(CLEAN_VIDEO_TOTAL_SIZE, 0);
        } else {
            int i = 0;
            while (true) {
                int i2 = i;
                if (i2 >= arrayList.size()) {
                    break;
                }
                if (arrayList.get(i2) != null) {
                    j += arrayList.get(i2).getSize();
                }
                i = i2 + 1;
            }
            PrefsCleanUtil.getInstance().putLong(CLEAN_VIDEO_TOTAL_SIZE, j);
        }
        return arrayList;
    }

    private HashMap<String, String> shortVideoUrlList() {
        HashMap<String, String> hashMap = new HashMap();
        hashMap.put("/Android/data/com.ss.android.ugc.trill/cache/cache/", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_tiktok));
        hashMap.put("/Android/data/com.ss.android.ugc.aweme/cache/video/cache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_douyin));
        hashMap.put("/Android/data/com.ss.android.ugc.live/cache/video", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_fire));
        hashMap.put("/Android/data/com.smile.gifmaker/cache/.video_cache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_kuaishou));
        hashMap.put("/Android/data/com.smile.gifmaker/cache/.awesome_cache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_kuaishou));
        hashMap.put("/Android/data/com.tencent.karaoke/files/opus/tmp_cache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_quanmin_sing));
        hashMap.put("/Android/data/com.mobile.videonews.li.video/files/Download/LiVideo/offline_video_auto", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_li));
        hashMap.put("/Ingkee/shortVideo", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_yingke));
        hashMap.put("/Android/data/com.gotokeep.keep/files/Movies", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_keep_video));
        hashMap.put("/Android/data/com.tencent.qqmusic/files/ad/video", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_qqmusic_video));
        hashMap.put("/Android/data/com.tencent.qqmusic/cache/video_cache/local", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_qqmusic_video));
        hashMap.put("/UCDownloads/video/.apolloCache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_uc_video));
        hashMap.put("/Android/data/com.tencent.weishi/cache/video_cache/local", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_weishi_video));
        hashMap.put("/youku/playercache/adcache/uplay", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_youku));
        hashMap.put("/Android/data/tv.yixia.bobo/cache/video-cache-sdk", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_bobo));
        hashMap.put("/Android/data/com.meitu.meipaimv/cache/media_save", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_meipai));
        hashMap.put("/Android/data/com.quvideo.xiaoying/cache/video-cache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_xiaoying));
        hashMap.put("/Android/data/com.baidu.haokan/cache/video", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_haokan));
        hashMap.put("/yy_video/yy_transvod_video", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_yy));
        hashMap.put("/pptv/.vast_ad", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_pptv));
        hashMap.put("/baidu/video/ads_cache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_baidu));
        hashMap.put("/baidu/video/media_cache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_baidu));
        hashMap.put("/cn.xiaochuankeji.tieba/pic/audiocache/video-cache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_zuiyou));
        hashMap.put("/Android/data/com.kugou.fanxing/cache/sv/cache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_kugou_direc));
        hashMap.put("/lightsky/LocalServerCache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_kuai_video));
        hashMap.put("/Android/data/com.ss.android.ugc.livelite/cache/video", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_fire_speed_video));
        hashMap.put("/Android/data/com.sup.android.superb/cache/ttpreloader", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_ppxia_video));
        hashMap.put("/SoGame/.videoplayer", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_kuaishou_game));
        hashMap.put("/Android/data/com.sohu.sohuvideo/files/LOCALCACHE", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_souhu_short_video));
        hashMap.put("/Android/data/com.sohu.sohuvideo/files/OADCACHE", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_souhu_short_video));
        hashMap.put("/Android/data/com.sohu.sohuvideo/p2p/forp2p/tea_p2p_cache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_souhu_short_video));
        hashMap.put("/sohu/SohuVideo/p2p/forp2p/tea_p2p_cache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_souhu_short_video));
        hashMap.put("/Android/data/com.tencent.xiafan/cache/tencent_sdk_download", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_xiafan_short_video));
        hashMap.put("/kugou/mv/down_c", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_kugou_mv));
        hashMap.put("/kugou/mv/cache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_kugou_mv));
        hashMap.put("/Android/data/com.bokecc.dance/cache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_tangdou));
        hashMap.put("/IPaiVideo/\u89c6\u9891", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_tencent_time));
        hashMap.put("/Android/data/com.uxin.live/cache/video-cache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_kila));
        hashMap.put("/Android/data/com.sohu.newsclient/files/sohuCache/tea_cache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_souhunews_short_video));
        hashMap.put("/Xiaomi/WALI_LIVE/mediaCache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_xiaomi));
        hashMap.put("/huajiaoliving/videocache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_huajiao));
        hashMap.put("/Android/data/com.tencent.news/cache/tencent_sdk_download", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_tencent_news));
        hashMap.put("/Android/data/com.tencent.mtt/cache/tencent_sdk_download", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_tencent_browser));
        hashMap.put("/Android/data/com.tencent.reading/cache/tencent_sdk_download", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_tt_news));
        hashMap.put("/VideoCache/com.le123.ysdq/main", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_videoall));
        hashMap.put("/Android/data/com.taobao.taobao/cache/video-cache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_taobao));
        hashMap.put("/Android/data/qsbk.app/cache/qbvideo", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_qsbk));
        hashMap.put("/cn.xiaochuankeji.zuiyouLite/pic/audiocache/video-cache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_ppgx));
        hashMap.put("/Android/data/com.fun.tv.vsmart/files/vsmart/media", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_xiankan));
        hashMap.put("/Android/data/com.baidu.minivideo/cache/video", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_quanmin_short));
        hashMap.put("/Android/data/com.perfect.video/files/FriendCache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_kandian));
        hashMap.put("/qutui360/cache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_qutui));
        hashMap.put("/Android/data/com.netease.newsreader.activity/cache/video_cache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_wangyi));
        hashMap.put("/Android/data/com.ss.android.ugc.aweme/cache/cache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_douyin));
        hashMap.put("/Android/data/com.tencent.qgame/files/vod_cache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_qqdianjin));
        hashMap.put("/tencent/now/nowvideo_cache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_tecentnow));
        hashMap.put("/TianTianKan/video", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_youyou));
        hashMap.put("/Android/data/com.yixia.videoeditor/cache/tempMediaCache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_miaopai));
        hashMap.put("/baidu/flyflow/.video_cache/com.baidu.searchbox.lite", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_baidu_speed));
        hashMap.put("/sixrooms/videoCache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_shiliu_direc));
        hashMap.put("/yuntutv/ad", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_cloud_tv));
        hashMap.put("/Android/data/com.taobao.idlefish/cache/video-cache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_xianyu));
        hashMap.put("/Android/data/com.sohu.sohuvideo/PlayerMp4Cache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_souhu_short_video));
        hashMap.put("/Android/data/com.sohu.tv/files/OADCACHE", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_souhu_hd));
        hashMap.put("/Android/data/com.sohu.tv/files/LOCALCACHE", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_souhu_hd));
        hashMap.put("/Android/data/com.tmall.wireless/cache/video-cache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_tianmao_shortvideo));
        hashMap.put("/Android/data/com.wuba.zhuanzhuan/cache/short_video_cache/txvodcache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_zhuanzhuan_shortvideo));
        hashMap.put("/Android/data/com.kuaishou.nebula/cache/.video_cache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_kuaishou_speed_shortvideo));
        hashMap.put("/Android/data/com.sohu.infonews/cache/videocache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_souhu_news_shortvideo));
        hashMap.put("/Android/data/com.yiche.autoeasy/cache/video-cache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_yiche_shortvideo));
        hashMap.put("/Android/data/com.tencent.qqlive/files/videos_4qDSw", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_tencent_shortvideo));
        hashMap.put("/Android/data/com.coohua.xinwenzhuan/cache/video-cache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_tao_news_shortvideo));
        hashMap.put("/coohua/video_cache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_tao_news_shortvideo));
        hashMap.put("/coohua/image_cache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_tao_news_shortvideo));
        hashMap.put("/Android/data/com.com.baomihuawang.androidclient/cache/video-cache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_baomihua_shortvideo));
        hashMap.put("/Android/data/com.xike.yipai/cache/video-cache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_quduopai_shortvideo));
        hashMap.put("/yipai/videocache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_quduopai_shortvideo));
        hashMap.put("/Android/data/com.sohu.youju/files/cache/sofa_video", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_sofa_shortvideo));
        hashMap.put("/Android/data/com.android.VideoPlayer/cache/media_cache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_vivo_shortvideo));
        hashMap.put("/Android/data/com.qiyi.video/files/app/player/puma/ad_cache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_qiyi_shortvideo));
        hashMap.put("/Android/data/com.qiyi.video.sdkplayer/files/app/player/puma/ad_cache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_qiyi_shortvideo));
        hashMap.put("/Android/data/com.funshion.video.mobile/files/funshion/ad/video", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_wind_movie_shortvideo));
        hashMap.put("/ctrip.android.view/livestream/simpleplayer", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_xiecheng_shortvideo));
        hashMap.put("/Android/data/com.android.jianying/cache/video-cache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_jianying_shortvideo));
        hashMap.put("/Android/data/com.yiche.autoeasy/cache/video-cache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_yiche_shortvideo));
        hashMap.put("/Android/data/com.v1.video/cache/video-cache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_first_shortvideo));
        hashMap.put("/Android/data/com.caotu.toutu/cache/video-cache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_toutu_shortvideo));
        hashMap.put("/Android/data/com.shoujiduoduo.dj/cache/.Video/Data", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_djduo_shortvideo));
        hashMap.put("/Android/data/my.maya.android/cache/videocache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_duoshan_shortvideo));
        hashMap.put("/Android/data/com.bullet.messenger/cache/video-cache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_liaotianbao_shortvideo));
        hashMap.put("/LuPingDaShi/recommendcache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_luping_shortvideo));
        hashMap.put("/Android/data/com.happyteam.dubbingshow/cache/xiangkan/video", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_luping_shortvideo));
        hashMap.put("/Android/data/com.mampod.ergedd/files/video-cache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_erge_shortvideo));
        hashMap.put("/smartcall/download", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_kuying_shortvideo));
        hashMap.put("/Android/data/com.rumtel.mobiletv/cache/video-cache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_lookvideo_shortvideo));
        hashMap.put("/Android/data/com.kuaigeng.video/files/FriendCache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_kandian_shortvideo));
        hashMap.put("/Android/data/com.maibo.android.tapai/cache/video-cache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_tapai_shortvideo));
        hashMap.put("/LiveCloud/LocalServerCache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_kuaicut_shortvideo));
        hashMap.put("/Android/data/com.hisunflytone.android/cache/pre", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_miguquan_shortvideo));
        hashMap.put("/Android/data/cn.wmlive.hhvideo/cache/video-cache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_dongci_shortvideo));
        hashMap.put("/Android/data/com.kuaiyou.xiaoxinyl/cache/video-cache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_douke_shortvideo));
        hashMap.put("/Android/data/com.nice.live/cache/video-cache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_haozan_shortvideo));
        hashMap.put("/Android/data/com.meitu.meipailite/cache/media_save", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_meipai_shortvideo));
        hashMap.put("/Android/data/com.yidian.xiaomi/cache/download", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_yidian_shortvideo));
        hashMap.put("/Android/data/com.txj.play.free/files/downloads", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_freevideo_shortvideo));
        hashMap.put("/Android/data/com.kandian.shortgaoxiao/cache/video-cache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_kuaishou_gaoxiao_video));
        hashMap.put("/Android/data/com.yunfan.topvideo/cache/video-cache/cache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_ttvideo_shortvideo));
        hashMap.put("/Android/data/com.kw.leike/cache/.VideoCache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_boke_shortvideo));
        hashMap.put("/yy_video", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_chigua_shortvideo));
        hashMap.put("/Android/data/com.onlookers.android/cache/video-cache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_weiguan_shortvideo));
        hashMap.put("/Android/data/video.like/cache/kk/temp/kk_v_cache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_like_shortvideo));
        hashMap.put("/Android/data/cn.com.kanjian/cache/video-cache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_kanjian_shortvideo));
        hashMap.put("/Android/data/com.ttmv.bobo_client/cache/video-cache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_bobo_shortvideo));
        hashMap.put("/yunfanencoder/video", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_bobo_shortvideo));
        hashMap.put("/TZVideo/cache", WechatCleannerApp.AppContext.getResources().getString(R.string.clean_tongzhi_shortvideo));
        return hashMap;
    }

    public List<CleanShortVideoInfo> hasDirecFileVideo() {
        List<CleanShortVideoInfo> arrayList = new ArrayList();
        HashMap<String, String> shortVideoUrlList = shortVideoUrlList();
        if (shortVideoUrlList != null && shortVideoUrlList.size() > 0) {
            for (Map.Entry<String, String> entry: shortVideoUrlList.entrySet()) {
                File file = new File(Environment.getExternalStorageDirectory() + entry.getKey());
                if (file.exists()) {
                    shortVideoFileScan(file, entry.getValue(), arrayList);
                }
            }
        }
        return arrayList;
    }

    private void shortVideoFileScan(File file, String str, List<CleanShortVideoInfo> list) {
        File[] listFiles = file.listFiles();
        if (listFiles != null) {
            for (File file2 : listFiles) {
                if (file2 != null) {
                    if (file2.isDirectory()) {
                        shortVideoFileScan(file2, str, list);
                    } else if (file2.exists() && !file2.isDirectory()) {
                        try {
                            CleanShortVideoInfo cleanShortVideoInfo = new CleanShortVideoInfo();
                            cleanShortVideoInfo.setUpdateTime(file2.lastModified());
                            cleanShortVideoInfo.setSize(file2.length());
                            cleanShortVideoInfo.setTitle(file2.getName());
                            cleanShortVideoInfo.setUrl(file2.getPath());
                            cleanShortVideoInfo.setFromSoure(str);
                            cleanShortVideoInfo.setVideoType(1);
                            if (!TextUtils.isEmpty(file2.getName()) && (!(file2.length() <= 20480 || TextUtils.isEmpty(file2.getName()) || file2.getName().contains(".cfg")) || cleanShortVideoInfo.getFromSoure().equals(WechatCleannerApp.AppContext.getResources().getString(R.string.clean_xiangkan)))) {
                                CleanVideoforEvenBusInfo cleanVideoforEvenBusInfo = new CleanVideoforEvenBusInfo();
                                cleanVideoforEvenBusInfo.setVideoSize(file2.length());
                                cleanVideoforEvenBusInfo.setVideoType(1);
                                if (System.currentTimeMillis() - this.startScanShortVideoTime < 3000) {
                                    Thread.sleep(8);
                                }
                                if (this.mMusicLoaderLinstener != null) {
                                    this.mMusicLoaderLinstener.onLoadVideoInfo(cleanVideoforEvenBusInfo);
                                }
                                list.add(cleanShortVideoInfo);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }


    public int getDurationInt(String str) {
        String obj = null;
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        try {
            mediaMetadataRetriever.setDataSource(str);
            obj = mediaMetadataRetriever.extractMetadata(9);
            try {
                mediaMetadataRetriever.release();
            } catch (RuntimeException ignored) {
            }
        } catch (Exception e2) {
            try {
                mediaMetadataRetriever.release();
            } catch (RuntimeException ignored) {
            }
        } catch (Throwable th) {
            try {
                mediaMetadataRetriever.release();
            } catch (RuntimeException ignored) {
            }
            throw th;
        }
        if (TextUtils.isEmpty(obj)) {
            return 0;
        }
        return Integer.valueOf(obj);
    }

    private Bitmap createVideoThumbnail(String str) {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        try {
            mediaMetadataRetriever.setDataSource(str);
            bitmap = mediaMetadataRetriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_NEXT_SYNC);
            try {
                mediaMetadataRetriever.release();
            } catch (RuntimeException ignored) {
            }
        } catch (Exception e2) {
            mediaMetadataRetriever.release();
        }
        return bitmap;
    }


    private Bitmap getBitmapFromFile(String str) {
        Bitmap createVideoThumbnail = ThumbnailUtils.createVideoThumbnail(str, 1);
        if (createVideoThumbnail != null) {
            return ThumbnailUtils.extractThumbnail(createVideoThumbnail, (int) (this.metrics.density * 100.0f), (int) (this.metrics.density * 100.0f));
        }
        return BitmapFactory.decodeResource(WechatCleannerApp.AppContext.getResources(), R.color.color_abcef8);
    }

    public Uri getMusicUriById(long j) {
        return ContentUris.withAppendedId(this.contentUri, j);
    }
}
