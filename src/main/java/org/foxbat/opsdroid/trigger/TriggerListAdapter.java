package org.foxbat.opsdroid.trigger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.foxbat.opsdroid.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chlr on 10/11/14.
 */
public class TriggerListAdapter extends BaseAdapter implements Filterable  {

    List<TriggerListRecord> list,filtered_list;
    private static TriggerListAdapter instance;
    private static Context context;
    private TriggerNameFilter tgr_name_filter;
    private TriggerListAdapter() {

        list = new ArrayList<>(50);
        filtered_list = new ArrayList<>(10);
    }

    enum tigger_type  {ops_trigger_tm
        ,ops_trigger_time
        ,ops_trigger_temp
        ,ops_trigger_manual
        ,ops_trigger_forecast
        ,ops_trigger_fm
        ,ops_trigger_cron
        ,ops_trigger_appl_monitor};

    public static TriggerListAdapter getInstance(Context context){
       TriggerListAdapter.context = context;
       if (instance == null) {
           instance = new TriggerListAdapter();
       }
        return instance;
    }

    public static TriggerListAdapter getInstance(){
        if (instance == null) {
            instance = new TriggerListAdapter();
        }
        return instance;
    }

    @Override
    public int getCount() {
        return filtered_list.size();
    }

    @Override
    public Object getItem(int i) {
        return filtered_list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view ==  null) {
            LayoutInflater inflater = (LayoutInflater)TriggerListAdapter.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_image_view,viewGroup,false);
        }
        ((ImageView) view.findViewById(R.id.image)).setImageResource( getDrawableResource(tigger_type.valueOf(filtered_list.get(i).sys_class_name)));
        ((TextView)view.findViewById(R.id.list_text_view)).setText(filtered_list.get(i).name);
        return view;
    }

    public TriggerListRecord getRecord(int i) {
        return filtered_list.get(i);
    }



    private int getDrawableResource(tigger_type type) {
        switch (type) {
            case ops_trigger_cron:
                return R.drawable.cron;
            case ops_trigger_time:
                return R.drawable.time;
            default:
                return R.drawable.others;
        }
    }


    public void refreshData(JSONArray json_arr) {
        list.clear();
        for( int i = 0 ,len = json_arr.length();  i < len ; i++) {
            try {
                 JSONObject json = json_arr.getJSONObject(i);
                 list.add(new TriggerListRecord(json.getString("name"),json.getString("sys_class_name"),json.getString("sys_id")));
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        filtered_list = list;
    }



    public class TriggerListRecord {

        public final String name,sys_class_name,trigger_id;

        public TriggerListRecord(String name, String sys_class_name, String trigger_id) {
            this.name = name.toLowerCase();
            this.sys_class_name = sys_class_name;
            this.trigger_id = trigger_id;
        }


    }


    @Override
    public Filter getFilter() {
        if (tgr_name_filter == null) {
            tgr_name_filter = new TriggerNameFilter();
        }
        return tgr_name_filter;
    }

    private class TriggerNameFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint == null || constraint.length() == 0) {
                synchronized (this) {
                    results.values = list;
                    results.count = list.size();
                }
            }
            else {
                List<TriggerListRecord> textFilteredResult = new ArrayList<>();
                for ( TriggerListRecord record : list) {
                    if (record.name.toLowerCase().contains(constraint.toString().toLowerCase())) {
                        textFilteredResult.add(record);
                    }
                }
                results.values = textFilteredResult;
                results.count = textFilteredResult.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            filtered_list = (ArrayList<TriggerListRecord>)filterResults.values;
            notifyDataSetChanged();
        }
    }



}
