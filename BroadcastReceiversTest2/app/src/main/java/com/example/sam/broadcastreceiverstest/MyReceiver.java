package com.example.sam.broadcastreceiverstest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Toast toast = Toast.makeText(context, "received", Toast.LENGTH_SHORT);
        toast.show();

        //throw new UnsupportedOperationException("Not yet implemented");
    }
}
