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

