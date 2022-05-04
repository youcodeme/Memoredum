package com.example.memoredum.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.memoredum.NotificationHelper;

public class MyBroadcast extends BroadcastReceiver {
    private String TAG = "MyBroadcast";
    /**
     * use to send a notification
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Broadcast action", Toast.LENGTH_SHORT).show();
        Log.e(TAG, "onReceive: 1");
        String action = intent.getAction();
        if(intent.getAction().equals("NOTIFICATION")) {
            Log.e("----", action);
            String title = intent.getStringExtra("title");
            String content = intent.getStringExtra("content");
            NotificationHelper notificationHelper = new NotificationHelper(context);
            NotificationCompat.Builder builder = notificationHelper.getNotification(title, content);
            builder.build();
            notificationHelper.notify(1, builder);
            Toast.makeText(context, "Broadcast action", Toast.LENGTH_SHORT).show();
        }
    }
}