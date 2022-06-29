package com.ilovesshan.sms_receive_plugin_example;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;

public class PermissionUtils {
    public static final int REQUEST_CODE = 0;

    /**
     * Android6.0之后 动态获取权限
     *
     * @param activity    Activity
     * @param permissions 权限列表
     */
    public static void checkPermission(Activity activity, String[] permissions) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, new String[]{permission}, REQUEST_CODE);
                }
            }
        }
    }
}
