package com.ilovesshan.sms_receive_plugin_example;

import android.Manifest;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ilovesshan.sms_receive_plugin_example.receiver.SmsReceiver;

import java.util.HashMap;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugins.GeneratedPluginRegistrant;

public class MainActivity extends FlutterActivity implements FlutterPlugin, MethodChannel.MethodCallHandler {
    private static final String TAG = "MainActivity";

    private static final String CHANNEL_TAG = "sms_receive_listener";

    private static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";

    private static final String[] permissions = {Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS};
    private SmsReceiver smsReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 注册插件
        // GeneratedPluginRegistrant.registerWith(new FlutterEngine(this));

        // 注册广播
        registerSmsBroadcastReceiver();

        // 动态申请权限
        PermissionUtils.checkPermission(MainActivity.this, permissions);

    }

    private void registerSmsBroadcastReceiver() {
        smsReceiver = new SmsReceiver();
        IntentFilter intentFilter = new IntentFilter(SMS_RECEIVED_ACTION);
        registerReceiver(smsReceiver, intentFilter);

        // 注册回调接口 监听短信结果
        smsReceiver.setOnSmsResultReceivedListener(sms -> {
            Log.d(TAG, "onSmsResultReceived: " + sms.getSenderPhone());
            Log.d(TAG, "onSmsResultReceived: " + sms.getReceiveMessage());

            // 将监听结果通知给 flutter
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    new MethodChannel(getFlutterEngine().getDartExecutor().getBinaryMessenger(), CHANNEL_TAG).invokeMethod("on_sms_received", sms.toString());
                }
            });
        });
    }


    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
        Log.d(TAG, "onAttachedToEngine: 插件绑定...");
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        Log.d(TAG, "onDetachedFromEngine: 插件解绑...");
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        smsReceiver.setOnSmsResultReceivedListener(null);
        smsReceiver = null;
    }
}
