-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-ignorewarnings
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-keepattributes Signature
-dontwarn com.jcraft.jzlib.**
-keep class com.jcraft.jzlib.**  { *;}
-dontwarn sun.misc.**
-keep class sun.misc.** { *;}

-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.** { *;}

-dontwarn sun.security.**
-keep class sun.security.** { *; }

-dontwarn com.google.**
-keep class com.google.** { *;}

-dontwarn com.avos.**
-keep class com.avos.** { *;}

-keep public class android.net.http.SslError
-keep public class android.webkit.WebViewClient

-dontwarn android.webkit.WebView
-dontwarn android.net.http.SslError
-dontwarn android.webkit.WebViewClient

-dontwarn android.support.**

-dontwarn org.apache.**
-keep class org.apache.** { *;}

-dontwarn org.jivesoftware.smack.**
-keep class org.jivesoftware.smack.** { *;}

-dontwarn com.loopj.**
-keep class com.loopj.** { *;}

-dontwarn org.xbill.**
-keep class org.xbill.** { *;}

-keepattributes *Annotation*

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference

-dontwarn android.support.**
-dontwarn demo.*
-dontwarn **CompatHoneycomb

-keepattributes *Annotation*
-keepattributes JavascriptInterface

-keep class android.support.v4.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment
-keep class com.qihoo.gamead.res.** {*;}
-keep class com.qihoo.channel.** {*;}


-keepattributes Signature
-keep class com.accuvally.hdtui.model.** { *; }
-keep class org.apache.commons.** { *; }
-keep class com.umeng*.** {*; }
-keep class com.baidu.**{*;}

-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}

-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}

-keep class com.baidu.**{*;}
-keep class vi.com.gdi.bgl.**{*;}

-dontwarn com.umeng.**
-dontwarn com.umeng.message.**
-dontwarn com.igexin.**
-dontwarn com.zdworks.android.sdk.zdclock.sdk.**
-keep class com.zdworks.android.zdclock.sdk.**{*;}
-keep class com.igexin.**{*;}

-dontwarn org.apache.commons.**

-keep public class * extends com.umeng.**

-keep public class com.asqw.android{
public void Start(java.lang.String);
}

-keep class * implements android.os.Parcelable {
public static final android.os.Parcelable$Creator *;
}

-keep class com.qihoo.gamead.ui.webview.ClientCallWebViewInterface {
	    public *;
}

-keep class com.qihoo.gamead.ui.webview.WebViewCallClientInterface {
	  public *;
}

-keepclasseswithmembernames class * {
native <methods>;
}
-keepclasseswithmembers class * {
public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context);
}
-keepclasseswithmembers class * {
public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.app.Activity {
public void *(android.view.View);
}
-keepclassmembers class * extends android.support.v4.app.Fragment {
   public void *(android.view.View);
}
-keepclassmembers class * extends android.support.v4.app.FragmentActivity {
   public void *(android.view.View);
}
-keepclassmembers enum * {
public static **[] values();
public static ** valueOf(java.lang.String);
}
-keepclassmembers class **.R$* {
    public static <fields>;
}
-keepclassmembers class **{
	public void onEvent*(**);
}
