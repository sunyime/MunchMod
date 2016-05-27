package com.cyngn.munchmod.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

/**
 * Permissions Utils
 */
public class AndroidUtils {

    private static final boolean DEBUG = true;
    private static final String TAG = "AndroidUtils";

    private AndroidUtils() {}

    /**
     * Check whether all the permissions are there
     * @param context
     * @param permissions
     * @return
     */
    public static boolean hasPermissions(Context context, String[] permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) !=
                    PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * Start an activity
     */
    public static boolean startActivity(Context context, Intent intent) {
        if (intent != null) {
            try {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                context.startActivity(intent);
                return true;

            }catch(ActivityNotFoundException ex) {
                Log.e(TAG, " Activity not found for intent: " + intent, ex);
            }catch(Throwable ex) {
                Log.e(TAG, " Unable to start activity for intent: " + intent, ex);
            }
        }
        return false;

    }

    /**
     * Start a Call activity
     */
    public static boolean startCallActivity(Context context, String phone) {
        Intent i = new Intent(Intent.ACTION_CALL);
        i.setData(Uri.parse("tel:" + phone));
        return startActivity(context, i);
    }


    /**
     * Start an URI activity
     */
    public static boolean startUriActivity(Context context, Uri uri) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(uri);
        return startActivity(context, i);
    }

    /**
     * Start an URI activity
     */
    public static boolean startUriActivity(Context context, String url) {
        return startUriActivity(context, Uri.parse(url));
    }
}
