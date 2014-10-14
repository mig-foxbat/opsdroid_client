package org.foxbat.opsdroid.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import org.foxbat.opsdroid.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by chlr on 10/13/14.
 */
public class Utils {

    Context context;

    public Utils(Context context) {
        this.context = context;
    }


    public String[] getSelectedSyncValue() {
        List<String> output = new ArrayList<>(10);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String[] src_arr = this.context.getResources().getStringArray(R.array.task_status_array);
        String[] lookup_arr = this.context.getResources().getStringArray(R.array.task_status_array_values);
        Set<String> ref_arr = prefs.getStringSet("selected_status",null);
        for (int i = 0, len = src_arr.length; i <= len ; i++) {
            if (ref_arr.contains(src_arr[i])) {
                output.add(lookup_arr[i]);
            }
        }
        return output.toArray(new String[0]);
    }


}
