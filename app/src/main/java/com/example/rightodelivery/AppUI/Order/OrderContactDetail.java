package com.example.rightodelivery.AppUI.Order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.righto.AppModules.UserData;
import com.example.righto.AppUI.Dashboard.ActiveOrder;
import com.example.righto.AppUI.Dashboard.NoActiveOrder;
import com.example.righto.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;


public class OrderContactDetail extends Fragment {

    Button back;
    Button cancel;
    Button placeOrder;
    ProgressBar loading;
    TextView orderLog;
    TextView name;
    TextView phone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_contact_detail, container, false);

        placeOrder = view.findViewById(R.id.orderContact_placeOrder);
        phone = view.findViewById(R.id.orderContact_phone);
        name = view.findViewById(R.id.orderContact_name);
        back = view.findViewById(R.id.orderContact_back);
        cancel = view.findViewById(R.id.orderContact_cancel);
        loading = view.findViewById(R.id.orderContact_loading);
        orderLog = view.findViewById(R.id.orderContact_log);

        if( UserData.getOrderViewMode() == 0){
            name.setText( UserData.getTempOrderData("name").toString());
            phone.setText( UserData.getTempOrderData("phone").toString());
        }
        else{
            name.setText( UserData.getOrderData("name").toString());
            phone.setText( UserData.getOrderData("phone").toString());
        }

        if( UserData.getOrderViewMode() == 1){
            name.setEnabled(false);
            phone.setEnabled(false);
            placeOrder.setVisibility(View.GONE);
            cancel.setText("CANCEL");
        }

        if( UserData.getOrderViewMode() == 2){
            placeOrder.setText("UPDATE ORDER");
            cancel.setText("CANCEL");
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new OrderDetail())
                        .commit();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if( UserData.getOrderViewMode() == 0){
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new NoActiveOrder())
                            .commit();
                }else {
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new ActiveOrder())
                            .commit();
                }
            }
        });

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placeNewOrder();
            }
        });


        return view;
    }


    void placeNewOrder(){

        Date date = new Date();

        String orderId = null;
        if( UserData.getOrderViewMode() == 2){
            orderId = UserData.getUserData("lastOrder").toString();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
            UserData.addTempOrderData("date", formatter.format(date));
        }else{
            SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmss");
            orderId = formatter.format(date);
            UserData.addUserData("lastOrder", orderId);
            formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
            UserData.addTempOrderData("date", formatter.format(date));
        }

        UserData.addTempOrderData("status","Order Placed."); // means order placed.
        UserData.addTempOrderData("phone",phone.getText().toString());
        UserData.addTempOrderData("name",name.getText().toString());
        UserData.addTempOrderData("email",FirebaseAuth.getInstance().getCurrentUser().getEmail());


        FirebaseFirestore.getInstance().collection("R_ActOrd").document( orderId )
                .set( UserData.getTempOrderData() )
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if( task.isSuccessful() ){
                            UserData.setOrderData( UserData.getTempOrderData());
                            Toast.makeText(getContext(), "Order placed Sucessfully.", Toast.LENGTH_SHORT).show();

                            if( UserData.getOrderViewMode() != 2){
                                addActiveOrderToUserData();
                            }

                            getParentFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, new ActiveOrder())
                                    .commit();
                        }
                    }
                });
    }

    void addActiveOrderToUserData(){
        FirebaseFirestore.getInstance().collection("R_Cust").document( FirebaseAuth.getInstance().getCurrentUser().getEmail() )
                .set( UserData.getUserData() );
    }
}