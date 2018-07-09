package com.mdgd.commons.utilities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Process;
import android.support.v4.app.ActivityCompat;

/**
 * Created by Max
 * on 09/07/2018.
 */
public class PermissionsUtil {

    @TargetApi(16)
    public static boolean requestPermissionsIfNeed(Activity ctx, int requestCode, String... permissions) {
        boolean result = true;
        if(ctx == null){
            return false;
        }
        for(String p : permissions){
            if(ctx.checkPermission(p, Process.myPid(), Process.myUid()) != PackageManager.PERMISSION_GRANTED){
                result = false;
                askPermissions(ctx, requestCode, permissions);
            }
        }
        return result;
    }

    private static void askPermissions(Activity ctx, int requestCode, String[] permissions) {
        ActivityCompat.requestPermissions(ctx, permissions, requestCode);
    }

    public static boolean areAllPermissionsGranted(int[] grantResults) {
        boolean result = false;
        for (int i : grantResults) {
            if (i == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }
}
