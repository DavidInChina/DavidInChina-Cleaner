package com.wechat.files.cleaner.videoclean.utils;

import android.content.Context;
import android.text.TextUtils;

import com.wechat.files.cleaner.R;
import com.wechat.files.cleaner.WechatCleannerApp;


public class CleanShortVideoUtil {
    public static String shortVideoName;

    public static String getShortVideoName() {
        return shortVideoName;
    }

    public static void setShortVideoName(String str) {
        shortVideoName = str;
    }

    public static int getCurrentDrawable(String str, Context context) {
        if (!TextUtils.isEmpty((CharSequence) str)) {
            if (str.equals(context.getResources().getString(R.string.clean_douyin))) {
                return R.drawable.clean_douyin_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_fire))) {
                return R.drawable.clean_fire_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_kuaishou))) {
                return R.drawable.clean_kuaishou_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_quanmin_sing))) {
                return R.drawable.clean_quanmin_sing_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_li))) {
                return R.drawable.clean_li_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_yingke))) {
                return R.drawable.clean_yingke_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_tencent_news))) {
                return R.drawable.clean_tencent_news_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_tencent_browser))) {
                return R.drawable.clean_tencent_browser_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_keep_video))) {
                return R.drawable.clean_keep_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_tt_news))) {
                return R.drawable.clean_tt_news_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_qqmusic_video))) {
                return R.drawable.clean_qqmusic_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_weishi_video))) {
                return R.drawable.clean_weishi_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_uc_video))) {
                return R.drawable.clean_uc_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_souhu_short_video))) {
                return R.drawable.cleean_souhu_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_xiafan_short_video))) {
                return R.drawable.clean_xiafan_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_youku))) {
                return R.drawable.clean_youku_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_bobo))) {
                return R.drawable.clean_bobo_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_meipai))) {
                return R.drawable.clean_meipai_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_baidu))) {
                return R.drawable.clean_baidu_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_xiaoying))) {
                return R.drawable.clean_xiaoying_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_haokan))) {
                return R.drawable.clean_haokan_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_yy))) {
                return R.drawable.clean_yy_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_pptv))) {
                return R.drawable.clean_pptv_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_videoall))) {
                return R.drawable.clean_videoall_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_zuiyou))) {
                return R.drawable.clean_zuiyou_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_kugou_direc))) {
                return R.drawable.clean_kugou_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_kuai_video))) {
                return R.drawable.clean_kuai_video_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_fire_speed_video))) {
                return R.drawable.clean_fire_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_ppxia_video))) {
                return R.drawable.clean_ppxia_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_kuaishou_game))) {
                return R.drawable.clean_kuaishou_game_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_kugou_mv))) {
                return R.drawable.clean_kugou_music_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_tangdou))) {
                return R.drawable.clean_tangdou_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_tencent_time))) {
                return R.drawable.clean_tencent_time_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_kila))) {
                return R.drawable.clean_kila_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_xiangkan))) {
                return R.drawable.clean_xiangkan_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_souhunews_short_video))) {
                return R.drawable.clean_souhu_news_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_xiaomi))) {
                return R.drawable.clean_xiaomi_direc_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_huajiao))) {
                return R.drawable.clean_huajiao_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_taobao))) {
                return R.drawable.clean_taobao_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_qsbk))) {
                return R.drawable.clean_qsbk_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_ppgx))) {
                return R.drawable.clean_ppgx_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_xiankan))) {
                return R.drawable.clean_xiankan_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_quanmin_short))) {
                return R.drawable.clean_quanmin_short_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_kandian))) {
                return R.drawable.clean_kandian_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_qutui))) {
                return R.drawable.clean_qutui_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_wangyi))) {
                return R.drawable.clean_wangyi_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_qqdianjin))) {
                return R.drawable.clean_qq_game_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_tecentnow))) {
                return R.drawable.clean_tencent_now_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_youyou))) {
                return R.drawable.clean_youyou_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_miaopai))) {
                return R.drawable.clean_miaopai_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_baidu_speed))) {
                return R.drawable.clean_baidu_speed_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_shiliu_direc))) {
                return R.drawable.clean_shiliu_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_cloud_tv))) {
                return R.drawable.clean_clound_tv_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_xianyu))) {
                return R.drawable.clean_xianyu_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_souhu_video))) {
                return R.drawable.cleean_souhu_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_souhu_hd))) {
                return R.drawable.cleean_souhu_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_tianmao_shortvideo))) {
                return R.drawable.clean_tianmao_shortvideo_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_zhuanzhuan_shortvideo))) {
                return R.drawable.clean_zhuanzhuan_shortvideo_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_kuaishou_speed_shortvideo))) {
                return R.drawable.clean_kuaishou_speed_shortvideo_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_souhu_news_shortvideo))) {
                return R.drawable.clean_souhu_news_shortvideo_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_yiche_shortvideo))) {
                return R.drawable.clean_yiche_shortvideo_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_tencent_shortvideo))) {
                return R.drawable.clean_tengxun_video_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_tao_news_shortvideo))) {
                return R.drawable.clean_tao_news_shortvideo_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_baomihua_shortvideo))) {
                return R.drawable.clean_baomihua_shortvideo_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_quduopai_shortvideo))) {
                return R.drawable.clean_quduopai_shortvideo_icon;
            }
            if (str.equals(context.getResources().getString(R.string.clean_sofa_shortvideo))) {
                return R.drawable.clean_sofa_shortvideo_icon;
            }
            if (str.equals(WechatCleannerApp.AppContext.getResources().getString(R.string.clean_vivo_shortvideo))) {
                return R.drawable.clean_vivo_shortvideo_icon;
            }
            if (str.equals(WechatCleannerApp.AppContext.getResources().getString(R.string.clean_qiyi_shortvideo))) {
                return R.drawable.clean_qiyi_shortvideo_icon;
            }
            if (str.equals(WechatCleannerApp.AppContext.getResources().getString(R.string.clean_wind_movie_shortvideo))) {
                return R.drawable.clean_wind_movie_shortvideo_icon;
            }
            if (str.equals(WechatCleannerApp.AppContext.getResources().getString(R.string.clean_xiecheng_shortvideo))) {
                return R.drawable.clean_xiecheng_shortvideo_icon;
            }
            if (str.equals(WechatCleannerApp.AppContext.getResources().getString(R.string.clean_jianying_shortvideo))) {
                return R.drawable.clean_jianying_shortvideo_icon;
            }
            if (str.equals(WechatCleannerApp.AppContext.getResources().getString(R.string.clean_yiche_shortvideo))) {
                return R.drawable.clean_yiche_shortvideo_icon;
            }
            if (str.equals(WechatCleannerApp.AppContext.getResources().getString(R.string.clean_first_shortvideo))) {
                return R.drawable.clean_first_shortvideo_icon;
            }
            if (str.equals(WechatCleannerApp.AppContext.getResources().getString(R.string.clean_toutu_shortvideo))) {
                return R.drawable.clean_toutu_shortvideo_icon;
            }
            if (str.equals(WechatCleannerApp.AppContext.getResources().getString(R.string.clean_djduo_shortvideo))) {
                return R.drawable.clean_djduo_shortvideo_icon;
            }
            if (str.equals(WechatCleannerApp.AppContext.getResources().getString(R.string.clean_duoshan_shortvideo))) {
                return R.drawable.clean_duoshan_shortvideo_icon;
            }
            if (str.equals(WechatCleannerApp.AppContext.getResources().getString(R.string.clean_liaotianbao_shortvideo))) {
                return R.drawable.clean_liaotianbao_shortvideo_icon;
            }
            if (str.equals(WechatCleannerApp.AppContext.getResources().getString(R.string.clean_luping_shortvideo))) {
                return R.drawable.clean_luping_shortvideo_icon;
            }
            if (str.equals(WechatCleannerApp.AppContext.getResources().getString(R.string.clean_erge_shortvideo))) {
                return R.drawable.clean_erge_shortvideo_icon;
            }
            if (str.equals(WechatCleannerApp.AppContext.getResources().getString(R.string.clean_kuying_shortvideo))) {
                return R.drawable.clean_kuying_shortvideo_icon;
            }
            if (str.equals(WechatCleannerApp.AppContext.getResources().getString(R.string.clean_lookvideo_shortvideo))) {
                return R.drawable.clean_lookvideo_shortvideo_icon;
            }
            if (str.equals(WechatCleannerApp.AppContext.getResources().getString(R.string.clean_kandian_shortvideo))) {
                return R.drawable.clean_kandian_shortvideo_icon;
            }
            if (str.equals(WechatCleannerApp.AppContext.getResources().getString(R.string.clean_tapai_shortvideo))) {
                return R.drawable.clean_tapai_shortvideo_icon;
            }
            if (str.equals(WechatCleannerApp.AppContext.getResources().getString(R.string.clean_kuaicut_shortvideo))) {
                return R.drawable.clean_kuaicut_shortvideo_icon;
            }
            if (str.equals(WechatCleannerApp.AppContext.getResources().getString(R.string.clean_miguquan_shortvideo))) {
                return R.drawable.clean_miguquan_shortvideo_icon;
            }
            if (str.equals(WechatCleannerApp.AppContext.getResources().getString(R.string.clean_miguquan_shortvideo))) {
                return R.drawable.clean_miguquan_shortvideo_icon;
            }
            if (str.equals(WechatCleannerApp.AppContext.getResources().getString(R.string.clean_dongci_shortvideo))) {
                return R.drawable.clean_dongci_shortvideo_icon;
            }
            if (str.equals(WechatCleannerApp.AppContext.getResources().getString(R.string.clean_douke_shortvideo))) {
                return R.drawable.clean_douke_shortvideo_icon;
            }
            if (str.equals(WechatCleannerApp.AppContext.getResources().getString(R.string.clean_haozan_shortvideo))) {
                return R.drawable.clean_haozan_shortvideo_icon;
            }
            if (str.equals(WechatCleannerApp.AppContext.getResources().getString(R.string.clean_meipai_shortvideo))) {
                return R.drawable.clean_meipai_shortvideo_icon;
            }
            if (str.equals(WechatCleannerApp.AppContext.getResources().getString(R.string.clean_yidian_shortvideo))) {
                return R.drawable.clean_yidian_shortvideo_icon;
            }
            if (str.equals(WechatCleannerApp.AppContext.getResources().getString(R.string.clean_freevideo_shortvideo))) {
                return R.drawable.clean_freevideo_shortvideo_icon;
            }
            if (str.equals(WechatCleannerApp.AppContext.getResources().getString(R.string.clean_ttvideo_shortvideo))) {
                return R.drawable.clean_ttvideo_shortvideo_icon;
            }
            if (str.equals(WechatCleannerApp.AppContext.getResources().getString(R.string.clean_boke_shortvideo))) {
                return R.drawable.clean_boke_shortvideo_icon;
            }
            if (str.equals(WechatCleannerApp.AppContext.getResources().getString(R.string.clean_chigua_shortvideo))) {
                return R.drawable.clean_chigua_shortvideo_icon;
            }
            if (str.equals(WechatCleannerApp.AppContext.getResources().getString(R.string.clean_weiguan_shortvideo))) {
                return R.drawable.clean_weiguan_shortvideo_icon;
            }
            if (str.equals(WechatCleannerApp.AppContext.getResources().getString(R.string.clean_like_shortvideo))) {
                return R.drawable.clean_like_shortvideo_icon;
            }
            if (str.equals(WechatCleannerApp.AppContext.getResources().getString(R.string.clean_kanjian_shortvideo))) {
                return R.drawable.clean_kanjian_shortvideo_icon;
            }
            if (str.equals(WechatCleannerApp.AppContext.getResources().getString(R.string.clean_bobo_shortvideo))) {
                return R.drawable.clean_bobo_shortvideo_icon;
            }
            if (str.equals(WechatCleannerApp.AppContext.getResources().getString(R.string.clean_tongzhi_shortvideo))) {
                return R.drawable.clean_tongzhi_shortvideo_icon;
            }
            if (str.equals(WechatCleannerApp.AppContext.getResources().getString(R.string.clean_kuaishou_gaoxiao_video))) {
                return R.drawable.clean_kuaishou_gaoxiao_video_icon;
            }
        }
        return R.drawable.clean_meipai_icon;
    }
}
