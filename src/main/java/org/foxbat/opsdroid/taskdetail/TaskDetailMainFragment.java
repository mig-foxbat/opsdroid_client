package org.foxbat.opsdroid.taskdetail;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.foxbat.opsdroid.R;
import org.foxbat.opsdroid.task.TaskRecord;

/**
 * Created by chlr on 10/7/14.
 */
public class TaskDetailMainFragment extends Fragment {

    TaskRecord record;

    public TaskDetailMainFragment(TaskRecord record) {
        this.record = record;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.task_detail_main_frag, container, false);
        FragmentTabHost tabhost = (FragmentTabHost)v.findViewById(android.R.id.tabhost);
        tabhost.setup(this.getActivity(),this.getChildFragmentManager(),android.R.id.tabcontent);

        tabhost.addTab(
                tabhost.newTabSpec("tab1").setIndicator("Tab 1", null),
                TaskDetailTabFragment.class, null);
        tabhost.addTab(
                tabhost.newTabSpec("tab2").setIndicator("Tab 2", null),
                TaskDetailTabFragment.class, null);
        tabhost.addTab(
                tabhost.newTabSpec("tab3").setIndicator("Tab 3", null),
                TaskDetailTabFragment.class, null);
        return v;
    }
}