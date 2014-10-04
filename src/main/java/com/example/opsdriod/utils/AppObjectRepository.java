package com.example.opsdriod.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import com.example.opsdriod.model.TaskFilterModel;

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
