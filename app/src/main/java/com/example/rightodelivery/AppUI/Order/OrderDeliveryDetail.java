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
import com.example.righto.R;


public class OrderDeliveryDetail extends Fragment {

    ImageView mapButton;
    TextView mapLink;
    TextView address;
    TextView deliveryLog;
    Button next;
    Button back;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_delivery_detail, container, false);

        address = view.findViewById(R.id.delivery_address);
        mapLink =view.findViewById(R.id.delivery_mapLink);
        mapButton = view.findViewById(R.id.delivery_mapButton);
        deliveryLog = view.findViewById(R.id.delivery_log);
        next = view.findViewById(R.id.delivery_nextButton);
        back = view.findViewById(R.id.delivery_backButton);


        if( UserData.getOrderViewMode() == 0){
            address.setText( UserData.getTempOrderData("dropAddress").toString());
            mapLink.setText( UserData.getTempOrderData("dropMapLocation").toString());
        }
        else{
            address.setText( UserData.getOrderData("dropAddress").toString());
            mapLink.setText( UserData.getOrderData("dropMapLocation").toString());
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

                if( UserData.getOrderViewMode() == 1 ){
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new OrderPickupDetail())
                            .commit();
                }else {

                    UserData.addTempOrderData("dropAddress", address.getText().toString());
                    UserData.addTempOrderData("dropMapLocation", mapLink.getText().toString());

                    deliveryLog.setText("");
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new OrderPickupDetail())
                            .commit();
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if( UserData.getOrderViewMode() == 1 ){
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new OrderDetail())
                            .commit();
                }else {
                    if (validData()) {

                        UserData.addTempOrderData("dropAddress", address.getText().toString());
                        UserData.addTempOrderData("dropMapLocation", mapLink.getText().toString());

                        deliveryLog.setText("");
                        getParentFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new OrderDetail())
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
            if( address.getText().length() < 10) {
                valid = false;
                deliveryLog.setText("Delivery address too short.");
            }
            if( address.getText().length() > 1000) {
                valid = false;
                deliveryLog.setText("Pickup address too long.");
            }
        }
        else {
            valid = false;
            deliveryLog.setText("Enter a delivery address.");
        }

        if( !validMapLink()){
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
                    deliveryLog.setText("Enter a valid location share link.");
                    valid = false;
                }
            }catch (Exception e){
                deliveryLog.setText("Enter a valid location share link.");
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