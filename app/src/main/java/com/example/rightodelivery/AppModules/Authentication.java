package com.example.rightodelivery.AppModules;

import com.google.firebase.auth.FirebaseAuth;

public class Authentication {

    private static FirebaseAuth firebaseAuth=null;

    public static FirebaseAuth getAuthInstance(){
        if( firebaseAuth == null){
            firebaseAuth = FirebaseAuth.getInstance();
        }
        return firebaseAuth;
    }
}
