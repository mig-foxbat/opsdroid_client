package org.foxbat.opsdroid.taskdetail;

import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import org.foxbat.opsdroid.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by chlr on 10/10/14.
 */
public class TaskDetailHistoryAdapter extends BaseAdapter {

    List<TaskHistoryRecord> list;
    private static ListFragment fragment;
    private static TaskDetailHistoryAdapter instance;

    private TaskDetailHistoryAdapter() {
        list = new ArrayList<>(10);

    }

    public static TaskDetailHistoryAdapter getInstance() {
        if (instance == null)
            instance = new TaskDetailHistoryAdapter();
        return new TaskDetailHistoryAdapter();
    }

    public static TaskDetailHistoryAdapter getInstance(ListFragment fragment) {
        TaskDetailHistoryAdapter.fragment = fragment;
        if (instance == null)
            instance = new TaskDetailHistoryAdapter();
        return new TaskDetailHistoryAdapter();
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view ==  null) {
            LayoutInflater inflater = fragment.getLayoutInflater(null);
            view= inflater.inflate(R.layout.task_detail_history_listitem,viewGroup, false);
        }
        TaskHistoryRecord record  = list.get(i);
        ((TextView)view.findViewById(R.id.hist_queue_time)).setText(record.queued_time);
        ((TextView)view.findViewById(R.id.hist_start_time)).setText(record.start_time);
        ((TextView)view.findViewById(R.id.hist_end_time)).setText(record.end_time);
        ((TextView)view.findViewById(R.id.hist_duration)).setText(record.duration);
        return view;
    }

    public void refreshData(JSONArray json_arr) {
        list.clear();
        for(int i = 0,len = json_arr.length(); i < len; i++) {
            try {
                JSONObject json = json_arr.getJSONObject(i);
                list.add(new TaskHistoryRecord(json.getInt("task_ref_count"),json.getString("queued_time"),json.getString("start_time"),json.getString("end_time"),json.getString("duration")));
                Collections.sort(list,new HistoryRecordComparator());
                this.notifyDataSetChanged();
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private class TaskHistoryRecord {

        String start_time,end_time,queued_time,duration;
        int task_ref_count;

        public TaskHistoryRecord(int task_ref_count,String start_time,String end_time, String queued_time,String duration) {
            this.task_ref_count = task_ref_count;
            this.start_time = start_time;
            this.end_time = end_time;
            this.queued_time = queued_time;
            this.duration = duration;
        }


    }


    private class HistoryRecordComparator implements Comparator<TaskHistoryRecord> {

        @Override
        public int compare(TaskHistoryRecord taskHistoryRecord, TaskHistoryRecord taskHistoryRecord2) {
            return taskHistoryRecord.task_ref_count - taskHistoryRecord.task_ref_count;
        }
    }


}
