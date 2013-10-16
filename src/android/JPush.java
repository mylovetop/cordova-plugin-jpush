package com.jpush.jpush;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

import cn.jpush.android.api.JPushInterface;


public class JPush extends CordovaPlugin {

    private static String TAG = "Client Receiver";

    private final static List<String> methodList =
            Arrays.asList(
                    "init",
                    "stopPush",
                    "resumePush",
                    "setTags",
                    "setTagAlias",
                    "setAlias",
                    "getIncoming"
            );
    private ExecutorService executorService = Executors.newFixedThreadPool(1);
    private static JPush instance;

    public static String incomingAlert;
    public static Map<String, String> incomingExtras;


    public JPush() {
        instance = this;
    }

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
    }

    private static JSONObject notificationObject(String message,
                                                 Map<String, String> extras) {
        JSONObject data = new JSONObject();
        try {
            data.put("message", message);
            data.put("extras", new JSONObject(extras));
        } catch (JSONException e) {

        }
        return data;
    }

    static void raisePush(String message, Map<String, String> extras) {
        if (instance == null) {
            return;
        }
        JSONObject data = notificationObject(message, extras);
        String js = String.format("window.plugins.JPush.setNoticeData(%s);", data.toString());
        try {
            instance.webView.sendJavascript(js);
        } catch (NullPointerException e) {

        } catch (Exception e) {

        }
    }

    @Override
    public boolean execute(final String action, final JSONArray data,
                           final CallbackContext callbackContext) throws JSONException {
        if (!methodList.contains(action)) {
            return false;
        }
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Method method = JPush.class.getDeclaredMethod(action, JSONArray.class, CallbackContext.class);
                    method.invoke(JPush.this, data, callbackContext);
                } catch (Exception e) {
                }
            }
        });
        return true;
    }

    void init(JSONArray data, CallbackContext callbackContext) {
        try {
            // init 传参，是否 debug
            JPushInterface.setDebugMode(data.getString(0).equals("true"));
            JPushInterface.init(this.cordova.getActivity().getApplicationContext());
        } catch (JSONException e) {
            e.printStackTrace();
            callbackContext.error("Error reading alias JSON");
        }
    }

    void stopPush(JSONArray data, CallbackContext callbackContext) {
        JPushInterface.resumePush(this.cordova.getActivity().getApplicationContext());
    }

    void resumePush(JSONArray data, CallbackContext callbackContext) {
        JPushInterface.resumePush(this.cordova.getActivity().getApplicationContext());

    }


    void setTags(JSONArray data, CallbackContext callbackContext) {
        HashSet<String> tags = new HashSet<String>();
        try {
            String tagStr = data.getString(0);
            String[] tagArr = tagStr.split(",");
            for (String tag : tagArr) {
                tags.add(tag);
            }
            Set<String> validTags = JPushInterface.filterValidTags(tags);
            JPushInterface.setTags(this.cordova.getActivity().getApplicationContext(), validTags, null);
            callbackContext.success();
        } catch (JSONException e) {
            e.printStackTrace();
            callbackContext.error("Error reading tags JSON");
        }
    }

    void setAlias(JSONArray data, CallbackContext callbackContext) {
        Log.e("lincoln", "set alias start");
        try {
            String alias = data.getString(0);
            JPushInterface.setAlias(this.cordova.getActivity().getApplicationContext(), alias, null);
            Log.e("lincoln", "set alias:" + alias);
            callbackContext.success();
        } catch (JSONException e) {
            e.printStackTrace();
            callbackContext.error("Error reading alias JSON");
        }
    }

    void setTagAlias(JSONArray data, CallbackContext callbackContext) {
        HashSet<String> tags = new HashSet<String>();
        String alias;
        try {
            alias = data.getString(1);
            JSONArray tagsArr = data.getJSONArray(0);
            for (int i = 0; i < tagsArr.length(); i++) {
                tags.add(tagsArr.getString(i));
            }

            JPushInterface.setAliasAndTags(this.cordova.getActivity().getApplicationContext(), alias, tags);
            callbackContext.success();
        } catch (JSONException e) {
            e.printStackTrace();
            callbackContext.error("Error reading tagAlias JSON");
        }
    }

    void getIncoming(JSONArray data, CallbackContext callBackContext) {
        String alert = JPush.incomingAlert;
        Map<String, String> extras = JPush.incomingExtras;

        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("message", alert);
            jsonData.put("extras", new JSONObject(extras));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        callBackContext.success(jsonData);

        JPush.incomingAlert = "";
        JPush.incomingExtras = new HashMap<String, String>();
    }
}
