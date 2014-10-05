package org.foxbat.opsdroid.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by chlr on 9/20/14.
 */
public class AppObjectRepository {

    private static SharedPreferences preferences;
    private static Context context;

    public static void setAppSharedPreference(SharedPreferences preferences) {
        AppObjectRepository.preferences = preferences;

    }

    public static SharedPreferences getAppSharedPreferences() {
        return AppObjectRepository.preferences;
    }

    public static void setContextObject(Context context) {
        AppObjectRepository.context = context;
    }

    public static Context getContext() {
        return AppObjectRepository.context;
    }



}
