package com.cyngn.munchmod;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Preferences
 */
public class Prefs {
    private static final String TAG = "Prefs";
    private static final String UI_PREFS_NAME = "com.cyngn.munch";

    private Prefs() {};

    // Base
    static final String PREFS_PREFIX = "munch";


    private static String getPrefsKey(String key) {
        return PREFS_PREFIX + "." + key;
    }

    //TODO: define keys here
    public static final String KEY_COFFEE = "coffee";
    public static final String KEY_TEA = "tea";
    public static final String KEY_BOOZE = "booze";

    public static final String KEY_THAI = "thai";
    public static final String KEY_INDIAN = "tea";
    public static final String KEY_SANDWICHES = "sandwiches";

    /**
     * Get the share prefs
     * @param context
     * @return
     */
    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(UI_PREFS_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Set a boolean pref
     * @param key
     * @param value
     */
    public static void setBooleanPref(Context context, String key, boolean value) {
        SharedPreferences.Editor edit = getPreferences(context).edit();
        edit.putBoolean(getPrefsKey(key), value);
        edit.apply();
    }


    /**
     * Get a boolean pref
     * @param key
     */
    public static boolean getBooleanPref(Context context, String key) {
        SharedPreferences prefs = getPreferences(context);
        return prefs.getBoolean(getPrefsKey(key), false);
    }
}
