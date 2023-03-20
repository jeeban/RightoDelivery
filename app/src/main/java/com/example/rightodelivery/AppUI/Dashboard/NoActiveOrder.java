package com.example.rightodelivery.AppUI.Dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.rightodelivery.AppModules.UserData;
import com.example.rightodelivery.AppUI.Order.OrderSummary;
import com.example.rightodelivery.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

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

    ListView dashboardActiveOrderList;
    EventListener<DocumentSnapshot> activeOrderDataChange = new EventListener<DocumentSnapshot>() {
        @Override
        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

        }
    };





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_no_active_order, container, false);

        profilename = view.findViewById(R.id.dashboard_profile_name);
        profilephone = view.findViewById(R.id.dashboard_profile_phone);
        profileemail = view.findViewById(R.id.dashboard_profile_email);
        profileDetails =  view.findViewById(R.id.dashboard_profile);


        profilephone.setText(UserData.getUserData().get("phone").toString());
        profilename.setText(UserData.getUserData().get("name").toString());
        profileemail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());


        dashboardActiveOrderList = view.findViewById(R.id.dashboard_listview);

        profileDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new DashboardProfileUpdate())
                        .commit();
            }
        });


        FirebaseFirestore.getInstance().collection("R_ActOrd")
                .whereIn("deliveryBy", Arrays.asList("","miku"))
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        UserData.setOrderList(   value.getDocuments() );
                        if( value.getDocuments().size() != 0 ) {

                            ArrayList<String> orderList = new ArrayList<>();

                            for (DocumentSnapshot order : UserData.getOrderList()) {
                                //UserData.setOrderData( (Map<String, Object>) order.getData());
                                orderList.add(order.getId() + " - " + order.get("name"));
                            }

                            if (orderList.size() != 0) {
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), soup.neumorphism.R.layout.support_simple_spinner_dropdown_item, orderList);
                                dashboardActiveOrderList.setAdapter(adapter);

                                dashboardActiveOrderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        String orderId = orderList.get(i).substring(0, orderList.get(i).indexOf(' '));
                                        Toast.makeText(getActivity(), "Order id : " + orderId + " will be delivered.", Toast.LENGTH_SHORT).show();
                                        UserData.setOrderData(UserData.getOrderList(orderId).getData());
                                        getParentFragmentManager().beginTransaction()
                                                .replace(R.id.fragment_container, new OrderSummary())
                                                .commit();
                                    }
                                });
                            }
                        }
                    }
                });

        return view;
    }
}