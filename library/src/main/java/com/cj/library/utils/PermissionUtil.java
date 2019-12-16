package com.cj.library.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;

import java.util.ArrayList;

import androidx.core.app.ActivityCompat;

/**
 Create by chenjiao at 2019/12/5 0005
 描述：申请权限工具
 */
public class PermissionUtil {
    public static void requestPermissions(Activity activity, String[] permissions, int requestCode) {
        if (activity == null || permissions == null || permissions.length < 1) {
            return;
        }
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    public static ArrayList<String> checkPermissionResult(Context context, String[] resultPermissions, int[] grantResults) {
        ArrayList<String> deniedPermissions = new ArrayList<>();
        for (int i = 0; i < resultPermissions.length; i++) {
            String resultPermission = resultPermissions[i];
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {//权限被拒绝
                if (Build.VERSION.SDK_INT >= 23) {
                    if (resultPermission.equals(Manifest.permission.SYSTEM_ALERT_WINDOW) && Settings.canDrawOverlays(context)) {
                        continue;
                    }
                    if (resultPermission.equals(Manifest.permission.WRITE_SETTINGS) && Settings.System.canWrite(context)) {
                        continue;
                    }
                }
                deniedPermissions.add(resultPermission);
            }
        }

        return deniedPermissions;

    }

    public static String[] getDeniedPermissions(Context context, String[] requiredPermissions) {
        if (requiredPermissions == null || requiredPermissions.length < 1) {
            return null;
        }
        ArrayList<String> deniedPermissions = new ArrayList<>();
        for (String requiredPermission : requiredPermissions) {
            if (ActivityCompat.checkSelfPermission(context, requiredPermission) != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(requiredPermission);
            }
        }

        return deniedPermissions.toArray(new String[deniedPermissions.size()]);
    }
}
