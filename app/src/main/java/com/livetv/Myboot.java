package com.livetv;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Myboot extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Intent i = new Intent(context, Activity_Video.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startService(i);
        }

}
