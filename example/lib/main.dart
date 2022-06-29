import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:sms_receive_plugin/sms_receive_plugin.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';

  String _senderPhone = "";
  String _receiveMessage ="";

  @override
  void initState() {
    super.initState();
    initPlatformState();

    // 初始化 SmsReceivePlugin
    SmsReceivePlugin.init();

    // 监听短信验证码
    SmsReceivePlugin.setListener(new SmsReceivedListenner(onSmsReceived: (Map<String,String> payload) {
      final senderPhone = payload["senderPhone"];
      final receiveMessage = payload["receiveMessage"];

      _senderPhone = senderPhone!;
      _receiveMessage = receiveMessage!;
      setState(() {});
    }));

  }

  Future<void> initPlatformState() async {
    String platformVersion;
    try {
      platformVersion = await SmsReceivePlugin.platformVersion ?? 'Unknown platform version';
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('短信验证码自动填充插件'),
        ),
        body: Column(
          mainAxisSize: MainAxisSize.max,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text('当前运行版本: $_platformVersion', style: TextStyle(fontSize: 24)),
            SizedBox(height: 20),
            Text('短信发送号码: $_senderPhone', style: TextStyle(fontSize: 20)),
            SizedBox(height: 10),
            Text('短信内容: $_receiveMessage', style: TextStyle(fontSize: 20)),
          ],
        ),
      ),
    );
  }
}
