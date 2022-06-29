import 'dart:async';
import 'dart:convert';

import 'package:flutter/cupertino.dart';
import 'package:flutter/services.dart';

typedef OnSmsReceived = void Function(Map<String, String> payload);

class SmsReceivedListenner {
  OnSmsReceived onSmsReceived;

  SmsReceivedListenner({required this.onSmsReceived});
}

class SmsReceivePlugin {
  static const MethodChannel _channel = const MethodChannel('sms_receive_plugin');
  static const String _CHANNEL_TAG = "sms_receive_listener";
  static late SmsReceivedListenner _smsReceivedListenner;

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static init() {
    print('flutter 注册回调, 开始监听短信验证码发送...');
    new MethodChannel(_CHANNEL_TAG).setMethodCallHandler(_platformCallHandler);
  }

  static Future<dynamic> _platformCallHandler(MethodCall call) async {
    try {
      if (_smsReceivedListenner != null) {
        Map<String, String> smsMap = {
          "senderPhone": jsonDecode(call.arguments)["senderPhone"],
          "receiveMessage": jsonDecode(call.arguments)["receiveMessage"],
        };
        _smsReceivedListenner.onSmsReceived(smsMap);
      }
    } catch (e) {
      print(e);
    }
  }

  static void setListener(SmsReceivedListenner smsReceivedListenner) {
    _smsReceivedListenner = smsReceivedListenner;
  }
}
