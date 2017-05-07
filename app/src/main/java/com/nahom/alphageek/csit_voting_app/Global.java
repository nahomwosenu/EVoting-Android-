package com.nahom.alphageek.csit_voting_app;

import android.app.AlertDialog;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * Created by AlphaGeek on 5/7/2017.
 */

public class Global {
    static String ip="10.0.3.2:81";
    static String path="/voting/";
    static String server="http://"+ip+path;//http://10.0.3.2:81/voting/
    static String language="amharic";//or english
    static String voter="student";//or lecture

    public static String getMac(Context context){
        WifiManager manager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        String address = info.getMacAddress();
        return address;
    }
    public static void showDialog(Context context,String title,String message){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
