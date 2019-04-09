package com.livetv;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class NetworkConnection extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        System.out.println();
      if (intent.getAction()!=null){
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {

            if (isConnected(context)){
                Toast.makeText(context,"Network avilable", Toast.LENGTH_SHORT).show();;

            }else{
                Toast.makeText(context,"Network Not avilable", Toast.LENGTH_SHORT).show();;
            }
        }
        }
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
