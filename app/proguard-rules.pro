

#指定代码的压缩级别
-optimizationpasses 5
#忽略警告
-ignorewarning
#保护注解
-keepattributes *Annotation*


#保持哪些类不被混淆
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}
-keep class com.alipay.mobilesecuritysdk.*
-keep class com.j256.ormlite.**{*;}
-keep class com.umeng.**{*;}
-keep class com.android.**{*;}
-keep class com.jajale.watch.entityno.**{*;}
-keep class com.jajale.watch.listener.**{*;}
-keep class com.jajale.watch.utils.CMethod{*;}
-keep class com.jajale.watch.factory.**{*;}
-keep class com.jajale.watch.entity.** { *; }
-keep class com.jajale.watch.entitydb.** { *; }
#protobuf
-keep  class  com.google.protobuf.** { *; }
-keep  class  com.bjjajale.push.common.protocol.** { *; }



-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.support.v4.**
-keep public class com.android.vending.licensing.ILicensingService

#如果有引用v4包可以添加下面这行
-keep public class * extends android.support.v4.app.Fragment

#列出从 apk 中删除的代码
#-printusage unused.txt
#混淆前后的映射
-printmapping mapping.txt


##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }

##---------------End: proguard configuration for Gson  ----------


#保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}
#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
#保持自定义控件类不被混淆
-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}
#不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}
#友盟
-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#银联插件
-keep class com.payeco.android.plugin.** {*;}
-keep class org.apache.http.entity.mime.** {*;}
-dontwarn com.payeco.android.plugin.**

-keepclassmembers class com.payeco.android.plugin {
  public *;
}

-keepattributes *Annotation*
-keepattributes *JavascriptInterface*

#个信
-dontwarn com.igexin.**
-keep class com.igexin.**{*;}

#小米
#这里com.xiaomi.mipushdemo.DemoMessageRreceiver改成app中定义的完整类名
-dontwarn com.xiaomi.push.service.a.a
-keep class com.wctw.date.receiver.MyXiaoMiReceiver{*;}
-keep public class * extends android.content.BroadcastReceiver
-keepclasseswithmembernames class com.xiaomi.**{*;}



#pdf
-keep class com.artifex.mupdfdemo.** {*;}

#GSON
-keepattributes Signature
# Gson specific classes
-keep class com.google.gson.** { *; }

# Application classes that will be serialized/deserialized over Gson


#
#高德地图
#高德相关混淆文件
#3D 地图
-keep   class com.amap.api.mapcore.**{*;}
-keep   class com.amap.api.maps.**{*;}
-keep   class com.autonavi.amap.mapcore.*{*;}
#Location
-keep   class com.amap.api.location.**{*;}
-keep   class com.aps.**{*;}
#Service
-keep   class com.amap.api.services.**{*;}


#webview+js
# keep 使用 webview 的类
#-keepclassmembers class InsuranceArticleActivity {
#   public *;
#}
# keep 使用 webview 的类的所有的内部类
#-keepclassmembers   class com.jajale.watch.activity.InfoDetailActivity$*{
#    *;
#}

#butterknife
-dontwarn butterknife.internal.**
-keep class **$$ViewInjector { *; }
-keepnames class * { @butterknife.InjectView *;}

-keep class  com.sina.weibo.** { *; }
-keep class  com.sina.sso.** { *; }


-keep class com.tencent.mm.sdk.** {
   *;
}

-keep public class com.jajale.watch.activity.NewsPaperActivity

-keepclassmembers class * extends android.webkit.WebChromeClient {
public void openFileChooser(...);
}



#喜玛拉雅
-dontwarn okio.**
-keep class okio.** { *;}

-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** { *;}

-dontwarn com.ximalaya.ting.android.player.**
-keep class com.ximalaya.ting.android.player.** { *;}

-dontwarn com.google.gson.**
-keep class com.google.gson.** { *;}

-dontwarn org.litepal.**
-keep class org.litepal.** { *;}

-dontwarn android.support.**
-keep class android.support.** { *;}

-keep interface com.ximalaya.ting.android.opensdk.** {*;}
-keep class com.ximalaya.ting.android.opensdk.** { *; }




