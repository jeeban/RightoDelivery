package com.example.rightodelivery.AppUI.Dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.rightodelivery.AppModules.UserData;
import com.example.rightodelivery.AppUI.Order.ActiveOrderCheck;
import com.example.rightodelivery.AppUI.Order.OrderSummary;
import com.example.rightodelivery.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;

import soup.neumorphism.NeumorphCardView;


public class ActiveOrder extends Fragment {

    TextView profilephone;
    TextView profilename;
    TextView profileemail;
    soup.neumorphism.NeumorphCardView profileDetails;
    Button newOrderButton;
    NeumorphCardView lastOrderDetails;
    TextView orderStatus;
    TextView orderid;
    TextView orderDate;
    TextView orderDeliverBy;
    TextView lastOrderLine2;
    TextView lastOrderLine3;
    Button viewOrderButton;
    Button modifyOrderButton;
    Button cancelOrderButton;
    Button callDeliveryBoy;
    TextView dashboardLog;
    soup.neumorphism.NeumorphCardView cancelReason;
    Button confirmCancel;
    Button abortCancel;

    EventListener<DocumentSnapshot> activeOrderDataChange = new EventListener<DocumentSnapshot>() {
        @Override
        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
            System.out.println("+++++++++++++++++++++++++++++++data updated +++++++++++++++++++++++++++++++++++");
            UserData.setOrderData(value.getData());
            System.out.println(UserData.getOrderData());
            if( UserData.getOrderData()==null){
                System.out.println( "data listener also deleted data.");
                UserData.registration.remove();
                UserData.setActiveOrderListenerActive(false);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new ActiveOrderCheck())
                        .commit();
            }
            else {
                System.out.println("User data : "+UserData.getUserData());
                if (UserData.getOrderData("deliveryBoy").toString().equals("")) {
                    callDeliveryBoy.setVisibility(View.GONE);
                    orderDeliverBy.setText("A delivery boy will take your order soon.");
                } else {
                    callDeliveryBoy.setVisibility(View.VISIBLE);
                    orderDeliverBy.setText("Delivery Boy Assigned - " + UserData.getOrderData("deliveryBoy").toString());
                }

                orderStatus.setText("Status : "+UserData.getOrderData("status").toString());
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_active_order, container, false);

        profilename = view.findViewById(R.id.dashboard_profile_name);
        profilephone = view.findViewById(R.id.dashboard_profile_phone);
        profileemail = view.findViewById(R.id.dashboard_profile_email);

        viewOrderButton =  view.findViewById(R.id.activeOrder_viewOrder_button);
        modifyOrderButton =  view.findViewById(R.id.activeOrder_modifyOrder_button);
        cancelOrderButton =  view.findViewById(R.id.activeOrder_cancelOrder_button);
        orderDeliverBy = view.findViewById(R.id.activeOrderDashboard_OrderDeliverBy);
        orderid = view.findViewById(R.id.activeOrderDashboard_OrderId);
        orderDate = view.findViewById(R.id.activeOrderDashboard_OrderDate);
        orderStatus = view.findViewById(R.id.activeOrderDashboard_OrderStatus);
        callDeliveryBoy = view.findViewById(R.id.activeOrder_callDelivery_button);

        dashboardLog = view.findViewById(R.id.activeOrderDashboard_log);

        profilephone.setText(UserData.getUserData().get("phone").toString());
        profilename.setText(UserData.getUserData().get("name").toString());
        profileemail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        orderid.setText("Order ID : "+UserData.getUserData("lastOrder").toString());
        orderDate.setText("Order Time : "+UserData.getOrderData("date").toString());
        orderStatus.setText( " Order Status : "+UserData.getOrderData("status").toString());
        if( UserData.getOrderData("deliveryBoy").toString().equals("")){
            orderDeliverBy.setText( "A delivery boy will take your order soon.");
        }else{
            orderDeliverBy.setText( "Delivery Boy Assigned : "+UserData.getOrderData("deliveryBoy").toString());
        }



        if( !UserData.getActiveOrderListenerActive() ){
            UserData.setActiveOrderListenerActive(true);
            UserData.registration = FirebaseFirestore.getInstance().collection("R_ActOrd").document( UserData.getUserData("lastOrder").toString() )
                    .addSnapshotListener( activeOrderDataChange );
        }


        UserData.setOrderViewMode(-1);

        viewOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserData.setOrderViewMode(1); // 1 = view order, no editing
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new OrderSummary())
                        .commit();
            }
        });






        return view;
    }


    void cancelOrder(){
        //System.out.println(UserData.getOrderData().toString());

        System.out.println( "data listener also deleted data.");
        UserData.registration.remove();
        UserData.setActiveOrderListenerActive(false);

        FirebaseFirestore.getInstance().collection("R_ActOrd").document( UserData.getUserData("lastOrder").toString() ).delete();
        FirebaseFirestore.getInstance().collection("R_Ord").document( UserData.getUserData("lastOrder").toString() ).set( UserData.getOrderData());
        //UserData.setLastOrderData( UserData.getOrderData() );
        UserData.setOrderData( null );
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new NoActiveOrder())
                .commit();
    }
}