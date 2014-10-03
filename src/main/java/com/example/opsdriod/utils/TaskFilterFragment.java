package com.example.opsdriod.utils;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import com.example.opsdriod.ExpandableListAdapter;
import com.example.opsdriod.R;

/**
 * Created by chlr on 10/3/14.
 */
public class TaskFilterFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.taskfiltersettings,container,false);
//        ListView type = (ListView) view.findViewById(R.id.type_filter);
//       ListView status = (ListView) view.findViewById(R.id.status_filter);
//        ArrayAdapter<String> type_adapter = new ArrayAdapter<>(this.getActivity(),android.R.layout.simple_list_item_multiple_choice,getResources().getStringArray(R.array.task_type_array));
//        type.setAdapter(type_adapter);
//        ArrayAdapter<String> status_adapter = new ArrayAdapter<>(this.getActivity(),android.R.layout.simple_list_item_multiple_choice,getResources().getStringArray(R.array.statusTypes));
//        status.setAdapter(status_adapter);
         ExpandableListView explist = (ExpandableListView)view.findViewById(R.id.exp_list);
         explist.setAdapter(new ExpandableListAdapter());
        return view;
    }
}