package com.example.rightodelivery.AppUI.Order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.righto.AppModules.UserData;
import com.example.righto.R;


public class OrderDetail extends Fragment {

    Button next;
    Button back;
    TextView deliveryLog;
    TextView orderDetail;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_detail, container, false);

        orderDetail = view.findViewById(R.id.orderDesc_detail);
        deliveryLog = view.findViewById(R.id.orderDesc_log);
        next = view.findViewById(R.id.orderDesc_next);
        back = view.findViewById(R.id.orderDesc_back);

        if( UserData.getOrderViewMode() == 0){
            orderDetail.setText( UserData.getTempOrderData("orderDetail").toString());
        }
        else{
            orderDetail.setText( UserData.getOrderData("orderDetail").toString());
        }

        if( UserData.getOrderViewMode() == 1){  //view mode. no editing
            orderDetail.setEnabled(false);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if( UserData.getOrderViewMode() == 1){
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new OrderDeliveryDetail())
                            .commit();
                }else {
                    UserData.addTempOrderData("orderDetail", orderDetail.getText().toString());

                    deliveryLog.setText("");
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new OrderDeliveryDetail())
                            .commit();
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if( UserData.getOrderViewMode() == 1) {
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new OrderContactDetail())
                            .commit();
                }else{
                    if( validData() ) {
                        UserData.addTempOrderData("orderDetail", orderDetail.getText().toString());

                        deliveryLog.setText("");
                        getParentFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new OrderContactDetail())
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
        if( orderDetail.getText().length() != 0 ){
            if( orderDetail.getText().length() < 5) {
                valid = false;
                deliveryLog.setText("order detail too short.");
            }
            if( orderDetail.getText().length() > 1000) {
                valid = false;
                deliveryLog.setText("order detail too long.");
            }
        }
        else {
            valid = false;
            deliveryLog.setText("Enter order details.");
        }

        return valid;
    }
}