package com.example.rightodelivery.AppUI.Order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.rightodelivery.AppModules.UserData;
import com.example.rightodelivery.AppUI.Dashboard.ActiveOrder;
import com.example.rightodelivery.AppUI.Dashboard.NoActiveOrder;
import com.example.rightodelivery.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class ActiveOrderCheck extends Fragment {

    ProgressBar loading;
    TextView logText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_active_order_check, container, false);

        loading = view.findViewById(R.id.activeordercheck_loading);
        logText = view.findViewById(R.id.activeordercheck_log);

        getActiveOrderData();
        return view;
    }

    void getActiveOrderData(){
        getLastOrderData();
    }

    void getLastOrderData(){
        openDashboardWithNOActiveOrder();
    }




    void openDashboardWithActiveOrder(){
        //logText.setText("Active order found.");
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new ActiveOrder())
                .commit();
    }

    void openDashboardWithNOActiveOrder(){
        //logText.setText("No Active order found.");
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new NoActiveOrder())
                .commit();
    }
}