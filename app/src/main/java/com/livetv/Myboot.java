package com.livetv;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Myboot extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("reciveed boot ");
            if (intent.getAction()!=null) {
                switch (intent.getAction()) {

                    case Intent.ACTION_BOOT_COMPLETED:
                        //what you want to do
                        Intent i = new Intent(context, Activity_Video.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(i);
                        break;
                }
            }else{

            }
        }

}
