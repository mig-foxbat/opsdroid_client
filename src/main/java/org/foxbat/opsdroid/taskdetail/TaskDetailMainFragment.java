package org.foxbat.opsdroid.taskdetail;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.foxbat.opsdroid.R;

/**
 * Created by chlr on 10/7/14.
 */
public class TaskDetailMainFragment extends Fragment {

    int record_id;


    public TaskDetailMainFragment(int record_id) {
        this.record_id = record_id;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.task_detail_main_frag, container, false);
        FragmentTabHost tabhost = (FragmentTabHost)v.findViewById(android.R.id.tabhost);
        tabhost.setup(this.getActivity(),this.getChildFragmentManager(),android.R.id.tabcontent);
        Bundle bundle = new Bundle();
        bundle.putInt("position",record_id);
        tabhost.addTab(
                tabhost.newTabSpec("tab1").setIndicator("general", null),
                TaskDetailGeneralTab.class, bundle);
        tabhost.addTab(
                tabhost.newTabSpec("tab2").setIndicator("log", null),
                TaskDetailLogTab.class, bundle);
        tabhost.addTab(
                tabhost.newTabSpec("tab3").setIndicator("history", null),
                TaskDetailHistoryFragment.class, bundle);
        return v;
    }
}