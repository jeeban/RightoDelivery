package com.example.rightodelivery.AppUI.Dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.righto.AppModules.UserData;
import com.example.righto.AppUI.Order.OrderPickupDetail;
import com.example.righto.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

import soup.neumorphism.NeumorphCardView;


public class NoActiveOrder extends Fragment {

    TextView profilephone;
    TextView profilename;
    TextView profileemail;
    soup.neumorphism.NeumorphCardView profileDetails;
    Button newOrderButton;
    NeumorphCardView lastOrderDetails;
    TextView lastOrderStatus;
    TextView lastOrderid;
    TextView lastorderDate;
    TextView lastOrderLine1;
    TextView lastOrderLine2;
    TextView lastOrderLine3;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_no_active_order, container, false);

        profilename = view.findViewById(R.id.dashboard_profile_name);
        profilephone = view.findViewById(R.id.dashboard_profile_phone);
        profileemail = view.findViewById(R.id.dashboard_profile_email);
        profileDetails =  view.findViewById(R.id.dashboard_profile);
        newOrderButton =  view.findViewById(R.id.new_order_button);
        lastOrderDetails = view.findViewById(R.id.noOrderDashboard_lastOrderDetails);
        lastOrderid = view.findViewById(R.id.noOrderDashboard_lastOrderId);
        lastorderDate = view.findViewById(R.id.noOrderDashboard_lastOrderDate);
        lastOrderStatus = view.findViewById(R.id.noOrderDashboard_lastOrderStatus);
        lastOrderLine1 = view.findViewById(R.id.noOrderDashboard_lastOrderLine1);
        lastOrderLine2 = view.findViewById(R.id.noOrderDashboard_lastOrderLine2);
        lastOrderLine3 = view.findViewById(R.id.noOrderDashboard_lastOrderLine3);

        profilephone.setText(UserData.getUserData().get("phone").toString());
        profilename.setText(UserData.getUserData().get("name").toString());
        profileemail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        if( UserData.getLastOrderData() == null ){
            lastOrderDetails.setVisibility(View.GONE);
        }else {
            lastOrderDetails.setVisibility(View.VISIBLE);
            lastOrderid.setText( "Order ID : "+UserData.getUserData("lastOrder").toString());
            lastorderDate.setText("Order Time : "+UserData.getLastOrderData("date").toString());
            lastOrderStatus.setText( "Status : "+UserData.getLastOrderData("status").toString());

            if( !UserData.getLastOrderData("deliveryBoy").toString().equals("")) {
                lastOrderLine2.setText("Delivery Boy : " + UserData.getLastOrderData("deliveryBoy").toString());
                //lastOrderLine3.setText( "Delivery Boy contact - "+UserData.getLastOrderData("deliveryBoyContact").toString());
            }

            if( !UserData.getLastOrderData("remark").toString().equals("")){
                lastOrderLine1.setText( "Remark : "+UserData.getLastOrderData("remark").toString());
            }

        }



        UserData.setOrderViewMode(-1);

        profileDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new DashboardProfileUpdate())
                        .commit();
            }
        });


        newOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UserData.clearTempOrderData();
                UserData.setTempOrderData( new HashMap<>());
                UserData.setOrderViewMode(0); // 0 = new order
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new OrderPickupDetail())
                        .commit();
            }
        });

        return view;
    }
}