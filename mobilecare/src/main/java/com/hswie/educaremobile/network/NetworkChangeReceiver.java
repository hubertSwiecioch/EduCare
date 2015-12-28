package com.hswie.educaremobile.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.hswie.educaremobile.LoginActivity;

/**
 * Created by hswie on 12/28/2015.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {

        String status = NetworkUtil.getConnectivityStatusString(context);
        Toast.makeText(context, status, Toast.LENGTH_LONG).show();

        if(status.equals("Not connected to Internet")){

            Intent newIntent = new Intent();
            newIntent.setClassName(LoginActivity.class.getPackage().getName(),LoginActivity.class.getName());
            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(newIntent);
        }


    }
}
