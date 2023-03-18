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

import com.example.righto.AppModules.UserData;
import com.example.righto.AppUI.Dashboard.NoActiveOrder;
import com.example.righto.R;


public class OrderPickupDetail extends Fragment {

    ImageView mapButton;
    TextView mapLink;
    TextView pickupLog;
    Button next;
    Button back;
    TextView address;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_order_pickup_detail, container, false);

        address = view.findViewById(R.id.pickup_Address);
        mapLink =view.findViewById(R.id.pickup_maplink);
        mapButton = view.findViewById(R.id.pickup_mapButton);
        pickupLog = view.findViewById(R.id.pickup_log);
        next = view.findViewById(R.id.pickup_next_button);
        back = view.findViewById(R.id.pickup_back_button);

        if( UserData.getOrderViewMode() == 0){  //new mode.
            address.setText( UserData.getTempOrderData("pickAddress").toString());
            mapLink.setText( UserData.getTempOrderData("pickMapLocation").toString());
        }
        else{
            address.setText( UserData.getOrderData("pickAddress").toString());
            mapLink.setText( UserData.getOrderData("pickMapLocation").toString());
        }

        if( UserData.getOrderViewMode() == 1){  //view mode. no editing
            address.setEnabled(false);
            mapLink.setEnabled(false);
        }





        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGoogleMap();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pickupLog.setText("");
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new NoActiveOrder())
                        .commit();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if( UserData.getOrderViewMode() == 1 ){
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new OrderDeliveryDetail())
                            .commit();
                }else {
                    if (validData()) {

                        UserData.addTempOrderData("pickAddress", address.getText().toString());
                        UserData.addTempOrderData("pickMapLocation", mapLink.getText().toString());

                        pickupLog.setText("");
                        getParentFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new OrderDeliveryDetail())
                                .commit();
                    }
                }
            }
        });

        return view;
    }

    boolean validData(){
        boolean valid = true;

        //check address
        if( address.getText().length() != 0 ){
            if( address.length() < 10) {
                valid = false;
                pickupLog.setText("Pickup address too short.");
            }
            if( address.getText().length() > 1000) {
                valid = false;
                pickupLog.setText("Pickup address too long.");
            }
        }

        if( !validMapLink() ) {
            valid = false;
        }

        return valid;
    }



    boolean validMapLink(){
        boolean valid = true;
        String url = mapLink.getText().toString();
        if( url.length()!=0){
            //process url.
            try {
                url = url.substring(url.indexOf("https://"));
                if ((url.contains("https://maps.app.goo.gl/") && (url.length() == 41)) || (url.contains("https://goo.gl/maps/") && (url.length() == 37))) {
                    mapLink.setText(url);
                }else {
                    pickupLog.setText("Enter a valid location share link.");
                    valid = false;
                }
            }catch (Exception e){
                pickupLog.setText("Enter a valid location share link.");
                valid = false;
            }
        }

        return valid;
    }


    void OpenGoogleMap() {

        String url = "https://maps.google.com";

        if( validMapLink()) {
            if (mapLink.getText().length() != 0) {
                url = mapLink.getText().toString();
            }
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        Intent chooser = Intent.createChooser(intent, "Launch Maps");
        startActivity(chooser);
    }
}