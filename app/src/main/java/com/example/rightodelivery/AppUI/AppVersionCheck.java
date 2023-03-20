package com.example.rightodelivery.AppUI;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.rightodelivery.AppModules.App;
import com.example.rightodelivery.AppModules.UserData;
import com.example.rightodelivery.AppUI.Order.ActiveOrderCheck;
import com.example.rightodelivery.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AppVersionCheck extends Fragment {

    ProgressBar loading;
    TextView logText;
    Button appExit;
    Button appUpdate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_version_check, container, false);

        loading = view.findViewById(R.id.appcheck_loading);
        logText = view.findViewById(R.id.appcheck_log);
        appExit = view.findViewById(R.id.appcheck_exit_button);
        appUpdate = view.findViewById(R.id.appcheck_update_button);

        appExit.setVisibility(View.INVISIBLE);
        appUpdate.setVisibility(View.INVISIBLE);

        appUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPlayStore();
            }
        });

        appExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeApp();
            }
        });

        checkForLatestVersion();
        return view;
    }

    void checkForLatestVersion(){
        if( App.getLatestVersion() == null) {
            FirebaseFirestore.getInstance().collection("R_AppVer").document("Del")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            loading.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                App.setLatestVersion( task.getResult().getString("Ver"));
                                checkForLatestVersion();
                            } else {
                                logText.setText("Connection Error.\nRestart App with working internet connection.");
                                appExit.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        }
        else {

            if (App.getLatestVersion().contains( App.getAppVersion())) {
                //logText.setText("App Verified.");
                getProfileData();
            } else {
                logText.setText("App Update Available.\nPlease update app to continue.");
                appExit.setVisibility(View.VISIBLE);
                appUpdate.setVisibility(View.VISIBLE);
            }
        }
    }


    void openPlayStore(){
        Intent intent = new Intent( Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://play.google.com/store?hl=en"));
        Intent chooser = Intent.createChooser( intent, "Launch Play Store.");
        startActivity( chooser);
    }


    void closeApp(){
        getActivity().finish();
    }


    void getProfileData() {
        if (UserData.getUserData() == null) {
            System.out.println(FirebaseAuth.getInstance().getCurrentUser().getUid()+"++++++++++++++++++++++++++++++++");
            FirebaseFirestore.getInstance().collection("R_Del").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            if (task.isSuccessful()) {
                                if (task.getResult().getData() != null) {
                                    UserData.setUserData(task.getResult().getData());
                                }
                                updateProfileDetails();
                            }
                        }
                    });
        }else {
            updateProfileDetails();
        }
    }

    void updateProfileDetails(){

        boolean profileUpdated = true;
        if( UserData.getUserData() == null){
            profileUpdated = false;
        }
        else if( UserData.getUserData().get("phone") == null){
            profileUpdated = false;
        }
        else if( UserData.getUserData().get("name") == null){
            profileUpdated = false;
        }

        if( profileUpdated ){
            System.out.println("go to active order check.");
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ActiveOrderCheck())
                    .commit();
        }
        else {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ProfileUpdate())
                    .commit();
        }
    }
}