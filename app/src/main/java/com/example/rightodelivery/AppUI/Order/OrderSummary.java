package com.example.rightodelivery.AppUI.Order;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.rightodelivery.AppModules.UserData;
import com.example.rightodelivery.AppUI.Dashboard.ActiveOrder;
import com.example.rightodelivery.AppUI.Dashboard.NoActiveOrder;
import com.example.rightodelivery.R;

import java.util.HashMap;


public class OrderSummary extends Fragment {

    TextView pickAddress;
    TextView pickMap;
    ImageView pickMapButton;
    TextView dropAddress;
    TextView dropMap;
    ImageView dropMapButton;
    TextView orderDesc;
    TextView phone;
    TextView name;

    Button back;
    Button modify;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_summary, container, false);

        pickAddress = view.findViewById(R.id.orderSummar_pickupAddress);
        pickMap = view.findViewById(R.id.orderSummar_pickupMaplink);
        pickMapButton = view.findViewById(R.id.orderSummar_pickupMapButton);
        dropAddress = view.findViewById(R.id.orderSummar_DropAddress);
        dropMap = view.findViewById(R.id.orderSummar_drop_maplink);
        dropMapButton = view.findViewById(R.id.orderSummar_drop_mapButton);
        orderDesc = view.findViewById(R.id.orderSummar_orderDescription);
        phone = view.findViewById(R.id.orderSummar_phone);
        name = view.findViewById(R.id.orderSummar_name);
        back = view.findViewById(R.id.orderSummary_back_button);
        //modify = view.findViewById(R.id.orderSummary_change_button);


        pickAddress.setText(UserData.getOrderData("pickAddress").toString());
        pickMap.setText(UserData.getOrderData("pickMapLocation").toString());
        dropAddress.setText(UserData.getOrderData("dropAddress").toString());
        dropMap.setText(UserData.getOrderData("dropMapLocation").toString());
        orderDesc.setText(UserData.getOrderData("orderDetail").toString());
        phone.setText(UserData.getOrderData("phone").toString());
        name.setText(UserData.getOrderData("name").toString());

        pickMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( pickMap.getText().length() > 2 ){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse( pickMap.getText().toString()));
                    Intent chooser = Intent.createChooser(intent, "Launch Maps");
                    startActivity(chooser);
                }
            }
        });

        dropMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( dropMap.getText().length() > 2 ){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse( dropMap.getText().toString()));
                    Intent chooser = Intent.createChooser(intent, "Launch Maps");
                    startActivity(chooser);
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new NoActiveOrder())
                        .commit();
            }
        });






        return view;
    }
}