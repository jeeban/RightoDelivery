package com.example.rightodelivery.AppModules;

import com.google.firebase.firestore.ListenerRegistration;

import java.util.Map;

public class UserData {
    private static Map<String, Object> orderData = null;
    private static Map<String, Object> tempOrderData = null;
    private static Map<String, Object> lastOrderData = null;
    private static Map<String, Object> userData = null;
    private static Map<String, Object> tempUserData = null;
    private static int orderViewMode = -1;
    public static ListenerRegistration registration;
    private static boolean activeOrderListenerActive = false;

    public static void setActiveOrderListenerActive(boolean val){
        activeOrderListenerActive = val;
    }

    public static boolean getActiveOrderListenerActive(){
        return activeOrderListenerActive;
    }
    public static void setOrderViewMode( int mode ){
        orderViewMode = mode;
    }
    public static int getOrderViewMode(){
        return orderViewMode;
    }



    public static void addOrderData( String key, Object value ){
        orderData.put( key, value);
    }
    public static void setOrderData( Map<String, Object> data ){
        orderData = data;
    }
    public static Map<String, Object> getOrderData(){
        return orderData;
    }
    public static Object getOrderData( String key ){
        return orderData.getOrDefault(key,"");
    }



    public static void setLastOrderData( Map<String, Object> data ){
        lastOrderData = data;
    }
    public static Map<String, Object> getLastOrderData(){
        return lastOrderData;
    }
    public static Object getLastOrderData( String key ){
        return lastOrderData.getOrDefault(key,"");
    }



    public static void setUserData(Map<String, Object> data ){
        userData = data;
    }
    public static Map<String, Object> getUserData(){
        return userData;
    }
    public static Object getUserData( String key){
        return userData.getOrDefault(key,"");
    }
    public static void addUserData( String key, Object value ){
        userData.put( key, value);
    }




    public static Map<String, Object> getTempOrderData(){
        return tempOrderData;
    }
    public static void clearTempOrderData(){
        tempOrderData = null;
    }
    public static void addTempOrderData( String key, Object value ){
        tempOrderData.put( key, value);
    }
    public static Object getTempOrderData( String key ){
        if( key.equals("phone")){
            return tempOrderData.getOrDefault(key,UserData.getUserData("phone"));
        }else if( key.equals("name")){
            return tempOrderData.getOrDefault(key,UserData.getUserData("name"));
        }
        return tempOrderData.getOrDefault(key,"");
    }
    public static void setTempOrderData( Map<String, Object> data ){
        tempOrderData = data;
    }

}
