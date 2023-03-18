package com.example.rightodelivery.AppModules;

public class App {
    private static final String appVersion="1.0";
    private static String latestVersion=null;
    public static String getAppVersion(){
        return appVersion;
    }
    public static String getLatestVersion(){
        return latestVersion;
    }
    public static void setLatestVersion( String version){
        latestVersion = version ;
    }
}
