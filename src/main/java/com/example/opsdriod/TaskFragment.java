package com.example.opsdriod;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.*;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chlr on 9/17/14.
 */
public class TaskFragment extends OpsListFragment {

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
        View view = inflater.inflate(R.layout.taskfragment,container,false);
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