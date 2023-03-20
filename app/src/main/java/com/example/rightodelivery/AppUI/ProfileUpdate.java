package com.example.rightodelivery.AppUI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.rightodelivery.AppModules.UserData;
import com.example.rightodelivery.AppUI.Dashboard.NoActiveOrder;
import com.example.rightodelivery.AppUI.Login.LoginOptions;
import com.example.rightodelivery.AppUI.Order.ActiveOrderCheck;
import com.example.rightodelivery.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;


public class ProfileUpdate extends Fragment {

    Button cancel;
    Button update;
    TextView name;
    TextView phone;
    TextView email;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_update, container, false);

        name = view.findViewById(R.id.profileUpdate_name);
        phone = view.findViewById(R.id.profileUpdate_phone);
        email = view.findViewById(R.id.profileUpdate_email);
        update = view.findViewById(R.id.profileUpdate_update);
        cancel = view.findViewById(R.id.profileUpdate_cancel);

        if( UserData.getUserData() != null) {
            name.setText(UserData.getUserData().getOrDefault("name", "").toString());
            phone.setText(UserData.getUserData().getOrDefault("phone", "").toString());
        }else{
            name.setText( FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            phone.setText( FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        }
        email.setText( FirebaseAuth.getInstance().getCurrentUser().getEmail());
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().getCurrentUser().delete();
                FirebaseAuth.getInstance().signOut();
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new LoginOptions())
                        .commit();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    void updateProfile(){Toast.makeText(getContext(), "Data updated.", Toast.LENGTH_SHORT).show();
        String username = name.getText().toString();
        String useremail = email.getText().toString();
        String userphone = phone.getText().toString();

        //check data validity

        //update
        UserData.setUserData( new HashMap<>());
        UserData.addUserData("name", username);
        UserData.addUserData("phone", userphone);

        FirebaseFirestore.getInstance().collection("R_Del").document( FirebaseAuth.getInstance().getCurrentUser().getUid() )
                .set( UserData.getUserData() )
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if( task.isSuccessful()){

                            getParentFragmentManager().beginTransaction()
                                    //.replace(R.id.fragment_container, new ActiveOrderCheck())
                                    .replace(R.id.fragment_container, new NoActiveOrder())
                                    .commit();
                        }
                        else {
                            Toast.makeText(getContext(), "Data update failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}