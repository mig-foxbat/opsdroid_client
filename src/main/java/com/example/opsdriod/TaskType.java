package com.example.opsdriod;

import android.app.ListFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.example.opsdriod.model.DatabaseHandler;
import com.example.opsdriod.utils.AppObjectRepository;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chlr on 9/17/14.
 */
public class TaskType extends OpsListFragment {

    TaskTypeAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        adapter = new TaskTypeAdapter();
        SharedPreferences perfs = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        int datekey = perfs.getInt("datekey",Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(new Date())));
        adapter.refreshData(datekey);
        this.setListAdapter(adapter);
        Log.v(this.getClass().getName(),"checkpoint1");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tasktype,container,false);
        view.findViewById(R.id.task_type_refresh).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(this.getClass().getName(), "Refresh request detected " + view.getClass().getName());
            }
        });
        Spinner spinner = (Spinner)view.findViewById(R.id.task_type_list);
        spinner.setAdapter(new  ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item,new String[] {"Linux/Unix","Monitors","Windows","z/OS","Monitor","Task 6","Task 7"}));
        return view;
    }

    @Override
    public void refreshData() {
        SharedPreferences perfs = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        int datekey = perfs.getInt("datekey",Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(new Date())));
        adapter.refreshData(datekey);
        adapter.notifyDataSetChanged();
    }
}