package org.foxbat.opsdroid.task;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.foxbat.opsdroid.R;
import org.foxbat.opsdroid.model.TaskFilterModel;
import org.foxbat.opsdroid.task.ExpandableListAdapter;

/**
 * Created by chlr on 10/3/14.
 */
public class TaskFilterFragment extends Fragment {

    org.foxbat.opsdroid.task.ExpandableListAdapter adapter;
    TaskFilterModel task_filter_model;


    @Override
    public void onResume() {
        super.onResume();
        task_filter_model = TaskFilterModel.getInstance();
        task_filter_model.deserializeFilterSettings();
    }

    @Override
    public void onPause() {
        super.onPause();
        task_filter_model.serializeFilterSettings();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.taskfiltersettings,container,false);
        final ExpandableListView explist = (ExpandableListView)view.findViewById(R.id.exp_list);
        adapter = new ExpandableListAdapter();
        explist.setAdapter(adapter);
        explist.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View childview, int group_position, int child_position, long l) {
                CheckedTextView checkedtext  = (CheckedTextView) childview;
                checkedtext.setChecked(!checkedtext.isChecked());
                task_filter_model.setCheckStatus(group_position,child_position,checkedtext.isChecked());
                return true;
            }
        });
        return view;
    }
}