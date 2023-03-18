package com.example.rightodelivery.AppUI.Order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.righto.AppModules.UserData;
import com.example.righto.AppUI.Dashboard.ActiveOrder;
import com.example.righto.AppUI.Dashboard.NoActiveOrder;
import com.example.righto.R;
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
        if( UserData.getOrderData() == null) {
            System.out.println(UserData.getUserData().toString()+"-------------------------------");
            System.out.println( "Last ORder : "+UserData.getUserData("lastOrder").toString()+"-------------------------------");

            String lastOrder = UserData.getUserData("lastOrder").toString();
            if( lastOrder.equals("") ){
                lastOrder = "x";
            }
            FirebaseFirestore.getInstance().collection("R_ActOrd").document( lastOrder )
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            loading.setVisibility(View.INVISIBLE);
                            if (task.isSuccessful()) {
                                if( task.getResult().getData() != null) {
                                    System.out.println("active order found. "+ task.getResult().getData() +"  "+UserData.getUserData("lastOrder").toString());
                                    //active order available
                                    UserData.setOrderData(task.getResult().getData());
                                    //UserData.setTempOrderData(task.getResult().getData());
                                    openDashboardWithActiveOrder();
                                }
                                else{
                                    System.out.println("active order not found. check order history");
                                    getLastOrderData();
                                }

                            }
                            else{
                                System.out.println("else part - active order not found. check order history");
                                getLastOrderData();
                            }
                        }
                    });
        }else{
            System.out.println("else part");
            openDashboardWithActiveOrder();
        }
    }

    void getLastOrderData(){
        if( UserData.getLastOrderData() == null) {

            String lastOrder = UserData.getUserData("lastOrder").toString();
            if( lastOrder.equals("") ){
                lastOrder = "x";
            }

            FirebaseFirestore.getInstance().collection("R_Ord").document( lastOrder )
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            loading.setVisibility(View.INVISIBLE);
                            if (task.isSuccessful()) {
                                if( task.getResult().getData() != null) {
                                    //active order available
                                    UserData.setLastOrderData(task.getResult().getData());
                                }
                                openDashboardWithNOActiveOrder();
                            }
                        }
                    });
        }else{
            openDashboardWithNOActiveOrder();
        }
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