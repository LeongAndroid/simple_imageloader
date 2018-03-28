package com.simple.imageloder.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by LeongAndroid on 2018/3/27.
 */

public class AppVersionUtils {
    /**
     * Get application version name,
     * in androidmainifest.xml android:versionName
     * @param context
     * @return
     */
    public static String getAppVersionName(Context context) {
        String name = null;
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo info = packageManager.getPackageInfo(context.getPackageName(), 0);
            if (info != null) {
                name = info.versionName;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    /**
     * Get application version code,
     * in androidmainifest.xml android:versionCode
     * @param context
     * @return
     */
    public static int getAppVersionCode(Context context) {
        int code = -1;
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo info = packageManager.getPackageInfo(context.getPackageName(), 0);
            if (info != null) {
                code = info.versionCode;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return code;
    }
}
