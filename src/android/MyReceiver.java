package com.jpush.jpush;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyReceiver extends BroadcastReceiver {
    private static String TAG = "Client Receiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {

        } else if (JPushInterface.ACTION_UNREGISTER.equals(intent.getAction())) {

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            handlingReceivedMessage(intent);
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            handlingNotificationOpen(context, intent);
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {

        } else {
            Log.d(TAG, "Unhandled intent - " + intent.getAction());
        }

    }

    private void handlingReceivedMessage(Intent intent) {
        String msg = intent.getStringExtra(JPushInterface.EXTRA_MESSAGE);
        Map<String, String> extras = getNotificationExtras(intent);

        JPush.raisePush(msg, extras);
    }

    private Map<String, String> getNotificationExtras(Intent intent) {
        Map<String, String> extrasMap = new HashMap<String, String>();

        for (String key : intent.getExtras().keySet()) {
            if (!IGNORED_EXTRAS_KEYS.contains(key)) {

                Log.e("Jpush key", "key:" + key);
                extrasMap.put(key, intent.getStringExtra(key));
            }
        }
        return extrasMap;
    }

    private void handlingNotificationOpen(Context context, Intent intent) {
        String alert = intent.getStringExtra(JPushInterface.EXTRA_ALERT);
        Map<String, String> extras = getNotificationExtras(intent);


        Intent launch = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        launch.addCategory(Intent.CATEGORY_LAUNCHER);
        launch.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        JPush.isFromAlert = true;
        JPush.incomingAlert = alert;
        JPush.incomingExtras = extras;

        context.startActivity(launch);
    }

    private static final List<String> IGNORED_EXTRAS_KEYS =
            Arrays.asList(
                    "app",
                    "cn.jpush.android.TITLE",
                    "cn.jpush.android.ALERT",
                    "cn.jpush.android.NOTIFICATION_CONTENT_TITLE",
                    "cn.jpush.android.NOTIFICATION_ID",
                    "cn.jpush.android.EXTRA",
                    "cn.jpush.android.MESSAGE",
                    "cn.jpush.android.APPKEY"
            );
}
