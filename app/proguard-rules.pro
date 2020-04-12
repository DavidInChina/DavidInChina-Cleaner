# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/sean/Android/Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# -- eventbus --
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

-keep class android.support.v4.** { *; }


# for assemble release
-dontwarn com.bumptech.glide.**
-dontwarn com.google.android.gms.**
-dontwarn org.greenrobot.eventbus.**
-dontwarn com.romainpiel.shimmer.**
-dontwarn me.relex.circleindicator.**
-dontwarn android.app.**
-dontwarn com.facebook.ads.**
-dontwarn com.facebook.**
-dontwarn com.google.android.exoplayer2.**
-dontwarn com.readystatesoftware.systembartint.**
-dontwarn de.morrox.fontinator.**
-dontwarn com.android.appcad.**
-dontwarn org.litepal.**
-dontwarn com.hzy.lib7z.**
-dontwarn com.thoughtbot.**
-dontwarn com.google.firebase.**
-dontwarn com.daimajia.**
#-dontwarn com.kaffnet.**
-dontwarn com.github.jinatonic.confetti.**
-keep class org.litepal.** {*; }
-keep class com.hzy.lib7z.** {*; }

-keep class android.content.pm.** { *; }
#-keep public class com.power.booster.app.db.mod.nt.SCNotificationAppInfo { *; }
#for glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
# for support v7
-dontwarn android.support.v7.**
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }

#for DAP
#-dontwarn com.duapps.ad.**
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep class com.dianxinos.DXStatService.stat.TokenManager {
    public static java.lang.String   getToken(android.content.Context);
}
-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class    * {
    @com.google.android.gms.common.annotation.KeepName *;
}
-keep class    com.google.android.gms.common.GooglePlayServicesUtil {
    public <methods>;
}
-keep class    com.google.android.gms.ads.identifier.AdvertisingIdClient {
    public <methods>;
}
-keep class    com.google.android.gms.ads.identifier.AdvertisingIdClient$Info {
    public <methods>;
}


#for umeng#
-keep class com.umeng.** {*;}
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-dontwarn com.umeng.**
-dontwarn tyrantgit.explosionfield.**

#for mysdk
-keep class com.android.inmob.data.bean.** { *; }
#for mopub#
-dontwarn com.mopub.**
# Keep public classes and methods.
-keepclassmembers class com.mopub.** { public *; }
-keep public class com.mopub.**
-keep public class android.webkit.JavascriptInterface {}

# Explicitly keep any custom event classes in any package.
-keep class * extends com.mopub.mobileads.CustomEventBanner {}
-keep class * extends com.mopub.mobileads.CustomEventInterstitial {}
-keep class * extends com.mopub.nativeads.CustomEventNative {}
-keep class * extends com.mopub.nativeads.CustomEventRewardedAd {}

# Keep methods that are accessed via reflection
-keepclassmembers class ** { @com.mopub.common.util.ReflectionTarget *; }

# Support for Android Advertiser ID.
-keep class com.google.android.gms.common.GooglePlayServicesUtil {*;}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient {*;}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info {*;}

# Support for Google Play Services
# http://developer.android.com/google/play-services/setup.html
-keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();
}

-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}

-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

#applovin proguard
-dontwarn com.applovin.**

#google GDPR
-dontwarn com.google.ads.consent.**
-keep class com.google.ads.consent.** { <fields>; }
-keepattributes *Annotation*
-keepattributes Signature

#for appsflyer
-keep class com.appsflyer.** {*;}
-dontwarn com.appsflyer.**
-dontwarn com.android.installreferrer
-ignorewarnings
#for lottie
-dontwarn com.airbnb.**

-printmapping mapping.txt

-keep public class * extends java.lang.Exception
#crashlytics
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**

#for avl sdk
-keep class com.avl.engine.** { *; }

#litepal
-keep class * extends org.litepal.crud.DataSupport {*;}

#butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **_ViewBinding { *; }
-keepclasseswithmembernames class * {     @butterknife.* <fields>;}
-keepclasseswithmembernames class * {     @butterknife.* <methods>;}

-keep class com.we.sdk.** {
}

-keep class com.bytedance.sdk.openadsdk.** { *; }
-keep public interface com.bytedance.sdk.openadsdk.downloadnew.** {*;}

# workmanager
#-keep class androidx.work.** { *; }
#-keep class android.arch.** { *; }
-keep class com.google.guava.** { *; }