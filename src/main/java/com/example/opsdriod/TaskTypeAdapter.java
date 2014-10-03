package com.example.opsdriod;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.opsdriod.model.DatabaseHandler;
import com.example.opsdriod.utils.AppObjectRepository;

import java.util.ArrayList;

/**
 * Created by chlr on 10/1/14.
 */
public class TaskTypeAdapter extends BaseAdapter {

    private ArrayList<Record> records;

    public TaskTypeAdapter() {
        records = new ArrayList<>(20);
    }

    @Override
    public int getCount() {
        return records.size();
    }

    @Override
    public Object getItem(int i) {
        return records.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Context context = AppObjectRepository.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View list_content_view = inflater.inflate(R.layout.listtextview,null);
        ((TextView)list_content_view.findViewById(R.id.list_text_view)).setText(records.get(i).taskname);
        Log.v(this.getClass().getName(),"set Text for View worked");
        return list_content_view;
    }

    public void refreshData(int datekey) {
        String sql = "SELECT distinct name,task_id FROM opswise_master WHERE datekey = %d";
        DatabaseHandler handler = new DatabaseHandler();
        Cursor cur = handler.executeQuery(String.format(sql, datekey), null);
        records.clear();
        for(cur.moveToFirst(); !cur.isAfterLast() ; cur.moveToNext()) {
            Log.i(this.getClass().getName(),cur.getString(0));
            records.add(new Record(cur.getString(0),cur.getString(1)));
        }
        cur.close();
    }


    private class Record
    {
        String taskname,sys_id;
        public Record(String taskname, String sys_id) {
            this.taskname = taskname;
            this.sys_id = sys_id;
        }

    }

}
