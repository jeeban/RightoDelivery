package com.example.rightodelivery.AppModules;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.List;
import java.util.Map;

public class UserData {
    private static List<DocumentSnapshot> orderList = null;
    private static Map<String, Object> orderData = null;
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


    public static void setOrderData( Map<String, Object> data ){
        orderData = data;
    }
    public static Map<String, Object> getOrderData(){
        return orderData;
    }
    public static Object getOrderData( String key ){
        return orderData.getOrDefault(key,"");
    }


    public static void setOrderList( List<DocumentSnapshot> list ){
        orderList = list;
    }
    public static DocumentSnapshot getOrderList( String key ){

        for( DocumentSnapshot order : orderList){
            if( order.getId().equals(key)){
                return order;
            }
        }
        return null;
        //return orderList.get(  orderList.indexOf(key));
    }
    public static List<DocumentSnapshot> getOrderList(){
        return orderList;
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
}
