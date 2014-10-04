package com.example.opsdriod.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import com.example.opsdriod.R;
import com.example.opsdriod.utils.AppObjectRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by chlr on 10/4/14.
 */
public class TaskFilterModel {

    private Boolean[] type_check_list,status_check_list;;
    private static final String TASK_TYPE_TAG = "TYPE_TAG";
    private static final String TASK_STATUS_TAG = "STATUS_TAG";
    private static final String APP_PREFERENCE_TAG = "TASK_FILTER_SETTING";
    Context context;
    private static TaskFilterModel instance = null;

    private TaskFilterModel() {
        this.context = AppObjectRepository.getContext();
        type_check_list = new Boolean[(this.context.getResources().getStringArray(R.array.task_type_array)).length];
        status_check_list = new Boolean[(this.context.getResources().getStringArray(R.array.task_status_array)).length];
        deserializeFilterSettings();
    }

    public static TaskFilterModel getInstance() {
        if (instance == null) {
            instance = new TaskFilterModel();
        }
        return instance;
    }


    public void setCheckStatus(int groupid, int childid, boolean status) {
        if (groupid == 0) {
            type_check_list[childid] = status;
        }
        else if(groupid == 1) {
            status_check_list[childid] = status;
        }
    }

    public boolean getCheckStatus(int groupid,int childid) {

        if (groupid == 0) {
            return type_check_list[childid];
        }
        else if(groupid == 1) {
            return status_check_list[childid];
        }
        else
            return false;
    }




    public void deserializeFilterSettings(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this.context);
        String json_string = pref.getString(APP_PREFERENCE_TAG, "");
        JSONObject json;
        try{
        if (!json_string.equals("")) {
             json = new JSONObject(json_string);
             JSONArray type_json_arr = json.getJSONArray(TASK_TYPE_TAG);
             for(int i =0; i < type_json_arr.length() ; i++) {
                 type_check_list[i] = type_json_arr.getBoolean(i);
             }
            JSONArray status_json_arr = json.getJSONArray(TASK_STATUS_TAG);
            for(int i =0; i < status_json_arr.length() ; i++) {
                status_check_list[i] = status_json_arr.getBoolean(i);
            }

            }
        else {
            for (int i = 0; i < type_check_list.length ; i++) {
                type_check_list[i] = false;
            }

            for (int i = 0; i < status_check_list.length ; i++) {
                status_check_list[i] = false;
            }
        }
        }
        catch (JSONException e) {
            Log.e(this.getClass().getName(), e.getMessage());
            e.printStackTrace();
        }

    }

    public void serializeFilterSettings() {
        SharedPreferences perf = PreferenceManager.getDefaultSharedPreferences(this.context);
        JSONObject json_content = new JSONObject();

        try {

            JSONArray type_json_arr = new JSONArray();
            for(int i = 0 ; i < type_check_list.length ; i++) {
                type_json_arr.put(type_check_list[i]);
            }
            json_content.put(TASK_TYPE_TAG, type_json_arr);


            JSONArray status_json_arr = new JSONArray();
            for(int i = 0 ; i < status_check_list.length ; i++) {
                status_json_arr.put(status_check_list[i]);
            }
            json_content.put(TASK_STATUS_TAG, status_json_arr);

            SharedPreferences.Editor editor = perf.edit();
            editor.putString(APP_PREFERENCE_TAG,json_content.toString(2));
            editor.commit();
        }
        catch (JSONException e) {

        }

    }









}
