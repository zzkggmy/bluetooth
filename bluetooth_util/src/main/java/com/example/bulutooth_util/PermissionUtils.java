package com.example.bulutooth_util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

public class PermissionUtils {
    private static class SingleInstance {
        private static final PermissionUtils instance = new PermissionUtils();
    }

    public static synchronized PermissionUtils getInstance() {
        return SingleInstance.instance;
    }

    public void checkPermissions(Activity activity, String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_DENIED) {
            } else {

            }
        }
    }
}
