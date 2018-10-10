# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\eclipse\android-sdk/tools/proguard/proguard-android.txt
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
-keepattributes *Annotation*
-keepattributes Signature
-dontwarn com.squareup.**
-keep class com.squareup.** { *; }

-keep class org.apache.http.** { *; }
-dontwarn org.apache.http.**
-dontwarn okio.**
-dontwarn org.apache.http.**
-dontwarn android.net.http.AndroidHttpClient
-dontwarn com.google.android.gms.**
-dontwarn com.android.volley.toolbox.**
-dontwarn io.card.**
-dontwarn android.support.**
-dontwarn android.net.SSLCertificateSocketFactory


# Basic proguard rules
-optimizations !code/simplification/arithmetic
-keepattributes <em>Annotation</em>
-keepattributes InnerClasses
-keepattributes EnclosingMethod
-keep class *<em>.R$</em>

-dontskipnonpubliclibraryclasses
-forceprocessing
-optimizationpasses 5
-overloadaggressively

# Removing logging code
-assumenosideeffects class android.util.Log {
public static *** d(...);
public static *** v(...);
public static *** i(...);
public static *** w(...);
public static *** e(...);
}

-dontwarn android.support.**
-dontwarn android.app.Notification
-dontwarn org.apache.log4j.**
-dontwarn com.google.common.**
