package com.example.rightodelivery.AppModules;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Fragment {
    private static FragmentManager fragmentManager=null;
    private static FragmentTransaction fragmentTransaction=null;

    public static void setFragmentManager( FragmentManager fragManager, FragmentTransaction fragTransaction){
        Fragment.fragmentManager = fragManager;
        Fragment.fragmentTransaction = fragTransaction;
    }

    public static FragmentTransaction getFragmentTransaction(){
        return Fragment.fragmentTransaction;
    }


}
