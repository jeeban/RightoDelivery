package com.example.rightodelivery;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.righto.AppUI.AppVersionCheck;
import com.example.righto.AppUI.Login.LoginOptions;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("Righto", "MainActivity() OnCreate()");

        /*
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            Log.d("Righto","MainActivity - No user. Go for Login.");
            textView.setText("Go to login page.");
            intent = new Intent( this, Login.class);
        }else{
            Log.d("Righto","MainActivity - Active user. Go for Dashboard.");
            textView.setText("Go to dashboard.");
            intent = new Intent( this, Dashboard.class);
            Log.d("righto","login found. :"+FirebaseAuth.getInstance().getCurrentUser().getEmail());
        }
        startActivity( intent);
        finish();

         */
        setContentView(R.layout.dashboard);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            fragmentTransaction.replace(R.id.fragment_container, new LoginOptions());
        }else{
            fragmentTransaction.replace(R.id.fragment_container, new AppVersionCheck());
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed(){

    }

}