package com.ilovesshan.sms_receive_plugin_example.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.ilovesshan.sms_receive_plugin_example.pojo.Sms;

/**
 * 监听短信验证码
 */
public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG = "SmsReceiver";

    OnSmsResultReceivedListener onSmsResultReceivedListener;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";

        if (intent.getAction().equals(SMS_RECEIVED_ACTION)) {

            StringBuilder content = new StringBuilder();//用于存储短信内容
            String sender = null;//存储短信发送方手机号
            Bundle bundle = intent.getExtras();//通过getExtras()方法获取短信内容
            String format = intent.getStringExtra("format");
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");//根据pdus关键字获取短信字节数组，数组内的每个元素都是一条短信
                for (Object object : pdus) {
                    SmsMessage message = SmsMessage.createFromPdu((byte[]) object, format);//将字节数组转化为Message对象
                    sender = message.getOriginatingAddress();//获取短信手机号
                    content.append(message.getMessageBody());//获取短信内容
                }
            }

            // 将结果通知出去
            if (onSmsResultReceivedListener != null) {
                Sms sms = new Sms(sender, content);
                onSmsResultReceivedListener.onSmsResultReceived(sms);
            }
        }
    }

    // 设置监听器
    public void setOnSmsResultReceivedListener(OnSmsResultReceivedListener onSmsResultReceivedListener) {
        this.onSmsResultReceivedListener = onSmsResultReceivedListener;
    }

    public interface OnSmsResultReceivedListener {
        void onSmsResultReceived(Sms sms);
    }
}
